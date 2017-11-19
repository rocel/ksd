<template>
    <div class="topic">
        <h1>TOPIC "{{topicName}}"</h1>
        <ul class="topicInfos">
            <li class="topicInfo" ref="nbpartitions">
                <h3>NUMBER OF PARTITIONS :</h3>
                <p>{{this.topicInfo.nbPartions}}</p>
            </li>
            <li class="topicInfo cleanuppolicy">
                <h3>CLEANUP POLICY :</h3>
                <p>{{this.topicInfo['cleanup.policy']}}</p>
            </li>
            <li class="topicInfo isinternal">
                <h3>IS INTERNAL :</h3>
                <p>{{this.topicInfo.isInternal}}</p>
            </li>
        </ul>
    </div>
</template>

<script type = "text/javascript" >
import api from '../api'

export default {
  name: 'topic',
  props: ['topicName'],

  watch: {
    $props: {
      handler: function (val, oldVal) {
        this.fetchTopicInfo()
      },
      deep: true
    }
  },
  data () {
    return {
      topicInfo: {}
    }
  },
  created () {
    this.fetchTopicInfo()
  },
  methods: {
    fetchTopicInfo () {
      console.log('Fetching topic info')
      api
        .get(`topics/${this.storeName}`)
        // .get(`services-ads`)
        .then(response => {
          this.topicInfo = response.data
        })
        .catch(err => console.log('Error : ', err))
    }
  }
}
</script>

<style>
.topic {
    position: absolute;
    top: 0;
    left: 300px;
    width: calc(100% - 300px);
    background-color: #ffffff;
    height: 100%;
    /* display: flex;
    align-items: center;
    justify-content: center; */
}
.topic h1 {
    display: block;
}
.topic .topicInfos  li {
    margin: 0 10px;
    padding: 50px;
}
.topic .topicInfos {
    list-style-type: none;
    padding: 0;
}
</style>