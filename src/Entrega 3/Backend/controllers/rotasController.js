

const db = require('../config/db')
const axios = require('axios')
const { GetRouteFromMapsAPI, reverseGeocode } = require('../services/mapsAPIService.js');
const Localizacao = require('../models/Localizacao.js');
const { where, or } = require('sequelize');

const ROUTES_API_URL = `${process.env.ROUTES_API_BASE_URL}/directions/v2:computeRoutes`;
const API_KEY = process.env.GOOGLE_MAPS_API_KEY 


exports.calcularRota = async (req, res) => {
    const { origem, destino } = req.body

    if (!origem || !destino) {
        return res.status(400).json({ error: 'Origem e destino são obrigatórios.' });
    }

    try {
        const apiResponse = await GetRouteFromMapsAPI(origem, destino)

        if (apiResponse.status !== 200) {
            return res.status(apiResponse.status).json({ error: 'Erro ao calcular rotas.' });
        }
        
        const routePromises = apiResponse.data.routes.map(async (route) => {
            await RegisterLocalizacoesInDB(route)
            const novaRota = await RegisterRouteInDB(route)
            await RegisterTrechosInDB(route, novaRota)

            return await db.Rota.findOne({ 
                where: { id_rota: novaRota.id_rota },
                include: [{
                    model: db.Trecho,
                    as: 'trechos',
                    attributes: ['id_trecho', 'descricao', 'distancia', 'duracao', 'polyline', 'order_number'],
                    include: [{
                        model: db.Localizacao,
                        as: 'local_partida',
                        attributes: ['id_localizacao', 'place_id', 'latitude', 'longitude', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
                    }, {
                        model: db.Localizacao,
                        as: 'local_destino',
                        attributes: ['id_localizacao', 'place_id', 'latitude', 'longitude',  'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
                    }],
                    order: [
                        ['order_number', 'ASC']
                    ]
                },
                {
                    model: db.Localizacao,
                    as: 'local_partida',
                    attributes: ['id_localizacao', 'place_id', 'latitude', 'longitude', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
                }, {
                    model: db.Localizacao,
                    as: 'local_destino',
                    attributes: ['id_localizacao', 'place_id', 'latitude', 'longitude', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
                }]
            })
        })

        const listaRotas = await Promise.all(routePromises)

        return res.status(200).json({routes: listaRotas});
    } catch (error) {
        console.error('Erro ao calcular a rota:', error);
        
        // Melhorar o log de erro para depuração
        if (error.response) {
            console.error('Resposta de erro da API:', {
                status: error.response.status,
                data: error.response.data
            });
        }
        
        return res.status(500).json({ 
            error: 'Erro ao calcular a rota.', 
            message: error.message 
        });
    }
}

exports.obterTrechosPorIdRota = async (req, res) => {
    const { id_rota } = req.params

    try {
        const trechos = await db.Trecho.findAll({
            // Incluir a localização associada ao trecho
            include: [{
                model: db.Localizacao,
                as: 'localPartida', 
                attributes: ['id_localizacao', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
            }, {
                model: db.Localizacao,
                as: 'localDestino', 
                attributes: ['id_localizacao', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
            }],
            where: {
                id_rota: id_rota
            }, order: [
                ['order_number', 'ASC']
            ],
        })

        if (!trechos || trechos.length === 0) {
            return res.status(404).json({ error: 'Nenhum trecho encontrado para esta rota.' });
        }

        return res.status(200).json(trechos)
    } catch (error) {
        console.log('Erro ao obter trechos:', error)
        return res.status(500).json({ error: 'Erro ao obter trechos: ' + error });
    }
}

exports.obterDetalhesLocal = async (req, res) => {
    try {
        const { latitude, longitude } = req.params;
        
        if (!latitude || !longitude) {
            return res.status(400).json({ error: 'Latitude e longitude são obrigatórios.' });
        }
        
        // URL para Geocodificação Reversa da Google
        const geocodingUrl = process.env.GEOCODING_API_BASE_URL;
        
        const response = await axios.get(geocodingUrl, {
            params: {
                latlng: `${latitude},${longitude}`,
                key: process.env.GOOGLE_MAPS_API_KEY,
                language: 'pt-BR' // Para resultados em português
            }
        });
        
        if (response.data.status !== 'OK') {
            return res.status(400).json({ 
                error: 'Erro na geocodificação reversa', 
                details: response.data.status 
            });
        }
        
        // Extrair os componentes do endereço
        const result = response.data.results[0];
        
        // Estruturar os dados para corresponder ao seu modelo de banco de dados
        const localInfo = {
            formatted_address: result.formatted_address,
            components: {}
        };
        
        // Processar os componentes do endereço
        result.address_components.forEach(component => {
            if (component.types.includes('route')) {
                localInfo.components.logradouro = component.long_name;
            } else if (component.types.includes('sublocality') || component.types.includes('sublocality_level_1') || component.types.includes('neighborhood') || component.types.includes('locality')) {
                localInfo.components.bairro = component.long_name;
            } else if (component.types.includes('administrative_area_level_2')) {
                localInfo.components.cidade = component.long_name;
            } else if (component.types.includes('administrative_area_level_1')) {
                localInfo.components.estado = component.long_name;
            } else if (component.types.includes('postal_code')) {
                localInfo.components.cep = component.long_name.replace('-', '');
            } else if (component.types.includes('country')) {
                localInfo.components.pais = component.long_name;
            }
        });
        
        // Adicionar as coordenadas precisas
        localInfo.latitude = result.geometry.location.lat;
        localInfo.longitude = result.geometry.location.lng;
        
        // Caso queira mais detalhes usando a Places API para obter informações adicionais
        if (result.place_id) {
            const placeDetailsUrl = 'https://maps.googleapis.com/maps/api/place/details/json';
            
            const placeResponse = await axios.get(placeDetailsUrl, {
                params: {
                    place_id: result.place_id,
                    key: process.env.GOOGLE_MAPS_API_KEY,
                    language: 'pt-BR',
                    fields: 'name,rating,formatted_phone_number,opening_hours,website,photos'
                }
            });
            
            if (placeResponse.data.status === 'OK') {
                localInfo.detalhes_lugar = placeResponse.data.result;
            }
        }
        
        return res.status(200).json(localInfo);
        
    } catch (error) {
        console.error('Erro ao obter detalhes do local:', error);
        return res.status(500).json({ 
            error: 'Erro ao obter detalhes do local', 
            message: error.message 
        });
    }
};

async function RegisterLocalizacoesInDB(response) {
    const locations = [];
    const { startLocation, endLocation, steps } = response.legs[0];
    
    locations.push(startLocation, endLocation);

    steps.forEach((step) => {
        locations.push(step.startLocation, step.endLocation);
    });

    // Usar Promise.all para processar todas as localizações de forma assíncrona
    await Promise.all(locations.map(async (location) => {
        try {
            const locationDetails = await reverseGeocode(
                location.latLng.latitude, 
                location.latLng.longitude
            );
            
            // Verificar se a localização já existe
            const existingLocation = await db.Localizacao.findOne({
                where: {
                    place_id: locationDetails.placeId
                }
            });

            // Se não existir, criar nova localização
            if (existingLocation === null) {
                // Se a localização já existe, não faz nada
                const localizacao = {
                    latitude: location.latLng.latitude,
                    longitude: location.latLng.longitude,
                    logradouro: `${locationDetails.componentes.logradouro || ''}, ${locationDetails.componentes.numero || ''}`.trim(),
                    bairro: locationDetails.componentes.bairro || '',
                    cidade: locationDetails.componentes.cidade || '',
                    estado: locationDetails.componentes.estado || '',
                    cep: locationDetails.componentes.cep || '',
                    pais: locationDetails.componentes.pais || 'Brasil',
                    place_id: locationDetails.placeId || '',
                };
    
                await db.Localizacao.create(localizacao);
            }
        } catch (error) {
            console.error('Erro ao processar localização:', error);
            // Não lançar erro para continuar processando outras localizações
        }        
    }));
}

async function RegisterRouteInDB(response) {
    const routeToken = response.routeToken
    const route = response.legs[0]
    let newStartLocation
    let newEndLocation

    try {
          
    } catch (error) {
        console.error('Erro ao verificar rota existente:', error)
        throw new Error('Erro ao verificar rota existente' + error)
    }
    
    try {
        const startLocationDetails = await reverseGeocode(route.startLocation.latLng.latitude, route.startLocation.latLng.longitude)
        
        const startLocation = await db.Localizacao.findOne({
            where: {
                latitude: route.startLocation.latLng.latitude,
                longitude: route.startLocation.latLng.longitude
            }
        })
        
        if (startLocation === null) {
            const localizacao = {
                latitude: route.startLocation.latLng.latitude,
                longitude: route.startLocation.latLng.longitude,
                logradouro: `${startLocationDetails.componentes.logradouro}, ${startLocationDetails.componentes.numero}`,
                bairro: startLocationDetails.componentes.bairro,
                cidade: startLocationDetails.componentes.cidade,
                estado: startLocationDetails.componentes.estado,
                cep: startLocationDetails.componentes.cep,
                pais: startLocationDetails.componentes.pais,
                place_id: startLocationDetails.placeId,
            }
            
            newStartLocation = await db.Localizacao.create(localizacao)
        } else {
            newStartLocation = startLocation
        }
        
        const endLocationDetails = await reverseGeocode(route.endLocation.latLng.latitude, route.endLocation.latLng.longitude)

        const endLocation = await db.Localizacao.findOne({
            where: {
                latitude: route.endLocation.latLng.latitude,
                longitude: route.endLocation.latLng.longitude
            }
        })

        if (endLocation === null) {
            const localizacao = {
                latitude: route.endLocation.latLng.latitude,
                longitude: route.endLocation.latLng.longitude,
                logradouro: `${endLocationDetails.componentes.logradouro}, ${endLocationDetails.componentes.numero}`,
                bairro: endLocationDetails.componentes.bairro,
                cidade: endLocationDetails.componentes.cidade,
                estado: endLocationDetails.componentes.estado,
                cep: endLocationDetails.componentes.cep,
                pais: endLocationDetails.componentes.pais,
                place_id: endLocationDetails.placeId,
            }
            
            newEndLocation = await db.Localizacao.create(localizacao)
        } else {
            newEndLocation = endLocation
        }

        const newRoute = await db.Rota.findOne({
            where: {
                descricao: `${newStartLocation.logradouro} - ${newEndLocation.logradouro}`,
                distancia: route.distanceMeters,
            }
        })

        if (newRoute !== null) {
            return newRoute
        } 

        const routeDetails = {
            maps_token: routeToken,
            polyline: route.polyline.encodedPolyline,
            id_local_partida: newStartLocation.id_localizacao,
            id_local_destino: newEndLocation.id_localizacao,
            descricao: `${newStartLocation.logradouro} - ${newEndLocation.logradouro}`,
            duracao: parseInt(route.duration.replace('s', '').trim()),
            distancia: route.distanceMeters,
        }

        return await db.Rota.create(routeDetails)
    } catch (error) {
        console.error('Erro ao registrar rota:', error)
        throw new Error("Erro ao registrar rota" + error);
    }
}

async function RegisterTrechosInDB(response, route) {
    const routeId = route.id_rota
    const { steps } = response.legs[0]

    try {
        var order = 1;
        // Usar Promise.all para processar todos os passos assincronamente
        await Promise.all(steps.map(async (step) => {
            // Verificar se o trecho já existe
            const existingStep = await db.Trecho.findOne({
                where: {
                    polyline: step.polyline.encodedPolyline,
                    id_rota: routeId
                }
            });
            
            if (existingStep !== null) {
                return
            }

            // Buscar ou criar a localização de início
            let startLocation = await db.Localizacao.findOne({
                where: {
                    latitude: step.startLocation.latLng.latitude,
                    longitude: step.startLocation.latLng.longitude
                }
            });

            // Se a localização de início não existe, obter detalhes e criá-la
            if (startLocation === null) {
                const startLocationDetails = await reverseGeocode(
                    step.startLocation.latLng.latitude, 
                    step.startLocation.latLng.longitude
                );
                
                // Criar nova localização
                const startLocalizacao = {
                    latitude: step.startLocation.latLng.latitude,
                    longitude: step.startLocation.latLng.longitude,
                    logradouro: `${startLocationDetails.componentes.logradouro || ''}, ${startLocationDetails.componentes.numero || ''}`.trim(),
                    bairro: startLocationDetails.componentes.bairro || '',
                    cidade: startLocationDetails.componentes.cidade || '',
                    estado: startLocationDetails.componentes.estado || '',
                    cep: startLocationDetails.componentes.cep || '',
                    pais: startLocationDetails.componentes.pais || 'Brasil',
                    place_id: startLocationDetails.placeId || '',
                };
                
                startLocation = await db.Localizacao.create(startLocalizacao);
            }

            // Buscar ou criar a localização de destino
            let endLocation = await db.Localizacao.findOne({
                where: {
                    latitude: step.endLocation.latLng.latitude,
                    longitude: step.endLocation.latLng.longitude
                }
            });

            // Se a localização de destino não existe, obter detalhes e criá-la
            if (endLocation === null) {
                const endLocationDetails = await reverseGeocode(
                    step.endLocation.latLng.latitude, 
                    step.endLocation.latLng.longitude
                );
                
                // Criar nova localização
                const endLocalizacao = {
                    latitude: step.endLocation.latLng.latitude,
                    longitude: step.endLocation.latLng.longitude,
                    logradouro: `${endLocationDetails.componentes.logradouro || ''}, ${endLocationDetails.componentes.numero || ''}`.trim(),
                    bairro: endLocationDetails.componentes.bairro || '',
                    cidade: endLocationDetails.componentes.cidade || '',
                    estado: endLocationDetails.componentes.estado || '',
                    cep: endLocationDetails.componentes.cep || '',
                    pais: endLocationDetails.componentes.pais || 'Brasil',
                    place_id: endLocationDetails.placeId || '',
                };
                
                endLocation = await db.Localizacao.create(endLocalizacao);
            }

            // Agora que temos certeza que ambas as localizações existem, podemos criar o trecho
            const trecho = {
                id_rota: routeId,
                id_local_partida: startLocation.id_localizacao,
                id_local_destino: endLocation.id_localizacao,
                polyline: step.polyline.encodedPolyline,
                order_number: order,
                descricao: `${startLocation.logradouro || 'Origem'} - ${endLocation.logradouro || 'Destino'}`,
                duracao: parseInt(step.staticDuration.replace('s', '').trim()),
                distancia: step.distanceMeters,
            };

            order++;
            return await db.Trecho.create(trecho);
        }));
    } catch (error) {
        console.error('Erro ao registrar trechos:', error);
        throw new Error('Erro ao registrar trechos: ' + error);
    }
}
