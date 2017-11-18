<template>
    <div class="table">
        <h1>{{storeName}}</h1>
        <datatable :perPage="[100, 10, 25, 50, 500]" :defaultPerPage="100" :sort="true" :columns="columns" :rows="rows" :printable="false" :exportable="false"></datatable>
    </div>
</template>

<script type = "text/javascript" >
import DataTable from 'vue-materialize-datatable'

export default {
  name: 'Table',
  props: ['storeName'],
  components: {
    datatable: DataTable
  },
  data () {
    return {
      columns: [
        {
          label: 'Name',
          field: 'name',
          numeric: false,
          html: false
        }
      ],
      rows: [
        {
          name: 'Test'
        }
      ]
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
h1 {
}
</style>