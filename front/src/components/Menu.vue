<template>
    <div class="menu">
        <router-link :to="{ name: 'Main' }"><h1>KSD</h1></router-link>
        <div class="topologyName">{{this.graph.topologyName}}</div>
        <h2>Topics</h2>
        <ul class="topics_list">
            <li v-for="topicNameItem in this.graph.topics"> <router-link :to="{ name: 'topics', params: {topicName: topicNameItem} }">{{topicNameItem}}</router-link></li>
        </ul>
        <h2>Stores</h2>
        <ul class="stores_list">
            <li v-for="storeNameItem in this.graph.stores"> <router-link :to="{ name: 'stores', params: {storeName: storeNameItem} }">{{storeNameItem}}</router-link></li>
        </ul>
    </div>
</template>

<script type = "text/javascript" >
import api from '../api'

export default {
  name: 'Menu',
  data () {
    return {
      graph: {}
    }
  },
  created () {
    this.fetchGraph()
  },
  methods: {
    fetchGraph () {
      console.log('Fetching graph')
      api.get('graph').then((response) => {
        this.graph = response.data
      })
      .catch((err) => console.log('Error : ', err))
    }
  }
}
</script>

<style>
.menu {
  top: 0;
  position: fixed;
  background-color: #39404e;
  width: 300px;
  height: 100%;
}
.menu h1,
h2 {
    font-weight: normal;
    color: #ffffff;
    text-align: center;
}
.menu .topologyName {
    color: white;
    border: 1px solid #eee;
    margin: 10px;
    padding: 10px;
}
.menu ul {
    list-style-type: none;
    padding: 0;
}
.menu li {
    margin: 0 10px;
}
.menu a {
    display: block;
    color: #ffffff;
    text-decoration: none;
    text-align: left;
    margin-bottom: 15px;
}
.menu a:hover {
    color: #aaa;
}
</style>