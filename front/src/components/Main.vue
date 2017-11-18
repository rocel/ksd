<template>
  <div class='main'>
    <menuC></menuC>
    <storeTableC v-if='storeName' v-bind:storeName='storeName'></storeTableC>
    <topicDataC v-else-if='topicName' v-bind:topicName='topicName'></topicDataC>
    <emptyC v-else></emptyC>
  </div>
</template>

<script>
import Menu from './Menu.vue'
import Empty from './Empty.vue'
import StoreTable from './StoreTable.vue'
import TopicData from './TopicData.vue'

export default {
  name: 'Main',
  components: {
    menuC: Menu,
    emptyC: Empty,
    storeTableC: StoreTable,
    topicDataC: TopicData
  },
  created () {
    if (this.$route.params.storeName) {
      this.storeName = this.$route.params.storeName
      console.log('Loading store data for :', this.$route.params.storeName)
    } else if (this.$route.params.topicName) {
      this.topicName = this.$route.params.topicName
      console.log('Loading topic data for :', this.$route.params.topicName)
    }
  },
  watch: {
    $route (to, from) {
      if (to.params.storeName) {
        this.topicName = null
        this.storeName = to.params.storeName
        console.log('Loading store data for :', this.storeName)
      } else if (to.params.topicName) {
        this.storeName = null
        this.topicName = to.params.topicName
        console.log('Loading topic data for :', this.topicName)
      }
    }
  },
  data () {
    return {
      storeName: null,
      topicName: null
    }
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>

</style>
