import axios from 'axios'

export default axios.create({
  baseURL: 'http://localhost:3000/', // change to /api
  timeout: 5000
})
