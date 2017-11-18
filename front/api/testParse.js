const data = require('./mock-api')

const cols = getColumns(data['tactics'])
// console.log(cols)
console.log(getRows(data.tactics))

function getRows(data) {
    const rows = []

    for (key in data) {
        rows.push(data[key])
    }

    return rows
}

function getColumns(data) {
    const columns = ['key']
    for (key in data) {
        const item = data[key]
        for (objKey in item) {
            if (columns.indexOf(objKey) < 0) {
                columns.push(objKey)
            }
        }
    }
    return columns
}