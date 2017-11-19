import axios from 'axios'

export default axios.create({
  baseURL: 'api', // baseURL: 'http://localhost:3000/'
  timeout: 5000
})
