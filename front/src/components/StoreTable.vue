<template>
    <div class="table">
      <h1>STORE "{{storeName}}"</h1>
      <data-tables :data="data" >
        <el-table-column v-for="column in columns" :prop="column.prop" :label="column.label" sortable="custom"></el-table-column>
      </data-tables>
    </div>
</template>

<script type = "text/javascript" >
// const dataTables = require(DataTables.default)

import api from '../api'

export default {
  name: 'Table',
  props: ['storeName'],
  components: {
    // datatable: DataTable
  },
  created () {
    this.fetchStoreInfo()
  },
  methods: {
    fetchStoreInfo () {
      console.log('Fetching store info')
      api
        .get('tactics')
        .then(response => {
          this.columns = this.getColumns(response.data)
          this.data = this.getRows(this.columns, response.data)
        })
        .catch(err => console.log('Error : ', err))
    },
    getRows (columns, data) {
      const rows = []
      for (let key in data) {
        const d = data[key]
        columns.forEach((column) => {
          if (typeof d[column.label] === 'undefined') {
            d[column.label] = 'n/a'
          }
        })
        d['key'] = key
        rows.push(d)
      }
      return rows
    },
    getColumns (data) {
      const columns = ['key']
      for (let key in data) {
        const item = data[key]
        for (let objKey in item) {
          if (columns.indexOf(objKey) < 0) {
            columns.push(objKey)
          }
        }
      }
      return columns.map((column) => {
        return {
          label: column,
          prop: column
        }
      })
    }
  },
  data () {
    return {
      data: [],
      columns: [],
      tableOptions: []
    }
  }
}
</script>

<style>
.table {
  position: absolute;
  top: 0;
  left: 300px;
  width: calc(100% - 300px);
  background-color: #ffffff;
  height: 100%;
}
.table h1 {
  color: #39404e;
}
</style>