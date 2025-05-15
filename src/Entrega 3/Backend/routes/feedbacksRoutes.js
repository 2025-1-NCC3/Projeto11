const express = require('express')
const router = express.Router()
const feedbacksController = require('../controllers/feedbacksController')

router.get('/', feedbacksController.obterFeedbacks)

module.exports = router