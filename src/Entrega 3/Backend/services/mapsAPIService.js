// controllers/placesController.js
const { Client } = require('@googlemaps/google-maps-services-js');
const client = new Client({});
const axios = require('axios');
const API_KEY = process.env.GOOGLE_MAPS_API_KEY;
const ROUTES_API_URL = `${process.env.ROUTES_API_BASE_URL}/directions/v2:computeRoutes`;

exports.GetRouteFromMapsAPI = async (origem, destino) => {
    const requestHeaders = {
        headers: {
            'Content-Type': 'application/json',
            'X-Goog-Api-Key': API_KEY,
            'X-Goog-FieldMask': '*'
        }
    }

    const requestData = {
        origin: {
            location: {
                latLng: {
                    latitude: origem.latitude,
                    longitude: origem.longitude
                }
            }
        },
        destination: {
            location: {
                latLng: {
                    latitude: destino.latitude,
                    longitude: destino.longitude
                }
            }
        },
        travelMode: 'DRIVE',
        routingPreference: 'TRAFFIC_AWARE',
        computeAlternativeRoutes: true,
        languageCode: 'pt-BR',
        units: "METRIC"
    }

    return await axios.post(ROUTES_API_URL, requestData, requestHeaders);
}

// Obter detalhes de um local pelo ID
// export async function getPlaceDetails(req, res) {
//     try {
//         const { placeId, sessiontoken } = req.query;
        
//         if (!placeId) {
//             return res.status(400).json({ error: 'O ID do local é obrigatório' });
//         }
        
//         const response = await client.placeDetails({
//             params: {
//                 place_id: placeId,
//                 key: process.env.GOOGLE_MAPS_API_KEY,
//                 sessiontoken: sessiontoken || undefined,
//                 language: 'pt-BR',
//                 fields: 'formatted_address,geometry,name,address_component'
//             }
//         });
        
//         if (response.data.status === 'OK') {
//             const place = response.data.result;
            
//             // Estruturar os componentes do endereço
//             const addressComponents = {};
//             if (place.address_components) {
//                 place.address_components.forEach(component => {
//                     if (component.types.includes('route')) {
//                         addressComponents.logradouro = component.long_name;
//                     } else if (component.types.includes('sublocality') || component.types.includes('sublocality_level_1')) {
//                         addressComponents.bairro = component.long_name;
//                     } else if (component.types.includes('administrative_area_level_2')) {
//                         addressComponents.cidade = component.long_name;
//                     } else if (component.types.includes('administrative_area_level_1')) {
//                         addressComponents.estado = component.long_name;
//                     } else if (component.types.includes('postal_code')) {
//                         addressComponents.cep = component.long_name.replace('-', '');
//                     } else if (component.types.includes('country')) {
//                         addressComponents.pais = component.long_name;
//                     }
//                 });
//             }
            
//             // Formatar o resultado final
//             const result = {
//                 placeId: placeId,
//                 nome: place.name,
//                 endereco: place.formatted_address,
//                 latitude: place.geometry?.location.lat,
//                 longitude: place.geometry?.location.lng,
//                 componentes: addressComponents
//             };
            
//             return res.json(result);
//         } else {
//             return res.status(400).json({ 
//                 error: 'Erro ao obter detalhes do local', 
//                 status: response.data.status 
//             });
//         }
//     } catch (error) {
//         console.error('Erro ao obter detalhes do local:', error);
//         return res.status(500).json({ error: 'Erro ao obter detalhes do local' });
//     }
// }


// Geocodificação reversa (coordenadas para endereço)
exports.reverseGeocode = async (latitude, longitude) => {
    try {
        if (!latitude || !longitude) {
            throw new Error('Latitude e longitude são obrigatórios');
        }
        
        const response = await client.reverseGeocode({
            params: {
                latlng: `${latitude},${longitude}`,
                key: API_KEY,
                language: 'pt-BR'
            }
        });
        
        if (response.data.status !== 'OK' && response.data.results.length <= 0) {
            throw new Error("Erro na geocodificação reversa: " + response.data.status);
        }
            
        const place = response.data.results[0];

        console.log(place);
        
        const addressComponents = {};
        if (place.address_components) {
            place.address_components.forEach(component => {
                if (component.types.includes('route')) {
                    addressComponents.logradouro = component.long_name;
                } else if (component.types.includes('street_number')) {
                    addressComponents.numero = component.long_name;
                } else if (component.types.includes('sublocality') || component.types.includes('sublocality_level_1')) {
                    addressComponents.bairro = component.long_name;
                } else if (component.types.includes('administrative_area_level_2')) {
                    addressComponents.cidade = component.long_name;
                } else if (component.types.includes('administrative_area_level_1')) {
                    addressComponents.estado = component.long_name;
                } else if (component.types.includes('postal_code')) {
                    addressComponents.cep = component.long_name.replace('-', '');
                } else if (component.types.includes('country')) {
                    addressComponents.pais = component.long_name;
                }
            });
        }
        
        // Formatar o resultado final
        const result = {
            placeId: place.place_id,
            endereco: place.formatted_address,
            latitude: parseFloat(latitude),
            longitude: parseFloat(longitude),
            componentes: addressComponents
        };
        
        return result;
    } catch (error) {
        console.error('Erro na geocodificação reversa:', error);
        throw new Error('Erro na geocodificação reversa: ' + error.message);
    }
}