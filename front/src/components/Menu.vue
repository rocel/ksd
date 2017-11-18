<template>
    <div class="menu">
        <h1>KSD</h1>
        <h2>Topics</h2>
        <ul class="topics_list">
            <li> <router-link :to="{ name: 'topics', params: {topicName: 'topic1'} }">Topic 1</router-link></li>
            <li> <router-link :to="{ name: 'topics', params: {topicName: 'topic2'} }">Topic 2</router-link></li>
            <li> <router-link :to="{ name: 'topics', params: {topicName: 'topic3'} }">Topic 3</router-link></li>
            <li> <router-link :to="{ name: 'topics', params: {topicName: 'topic4'} }">Topic 4</router-link></li>
        </ul>
        <h2>Stores</h2>
        <ul class="stores_list">
            <li> <router-link :to="{ name: 'stores', params: {storeName: 'store1'} }">Store 1</router-link></li>
            <li> <router-link :to="{ name: 'stores', params: {storeName: 'store2'} }">Store 2</router-link></li>
            <li> <router-link :to="{ name: 'stores', params: {storeName: 'store3'} }">Store 3</router-link></li>
            <li> <router-link :to="{ name: 'stores', params: {storeName: 'store4'} }">Store 4</router-link></li>
        </ul>
    </div>
</template>

<script type = "text/javascript" >
export default {
  name: 'Menu'
}
</script>

<style>
.menu {
  top: 0;
  position: absolute;
  background-color: #39404e;
  width: 300px;
  height: 100%;
}
h1,
h2 {
    font-weight: normal;
    color: #ffffff;
}
ul {
    list-style-type: none;
    padding: 0;
}
li {
    margin: 0 10px;
}
a {
    display: block;
    color: #ffffff;
    text-decoration: none;
    text-align: left;
    margin-bottom: 15px;
}
a:hover {
    color: #aaa;
}
</style>