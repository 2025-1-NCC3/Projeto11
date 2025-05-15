const db = require('../config/db')
const axios = require('axios')
const { Op } = require('sequelize')

exports.obterFeedbacks = async (req, res) => {
  try {
    const feedbacks = await db.Feedback.findAll();
    res.status(200).json(feedbacks)
  } catch (error) {
    console.error('Erro ao obter feedbacks:', error)
    res.status(500).json({ error: 'Erro ao obter feedbacks' })
  }
}