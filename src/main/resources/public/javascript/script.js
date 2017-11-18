fetch('./api/graph')
  .then(
    function(response) {
      if (response.status !== 200) {
        console.log('Looks like there was a problem. Status Code: ' +
          response.status);
        return;
      }

      // Examine the text in the response
      response.json().then(function(data) {
        document.querySelector('.content .topology_name').innerHTML = data.topologyName
        const topics = data.topics
        let topicsHtml = ''
        topics.forEach((topic) => {
            console.log(topic)
            topicsHtml += `<li>${topic}</li>`
        })
        document.querySelector('.content .topics_list').innerHTML =  topicsHtml
        const stores = data.stores
        let storesHtml = ''
        stores.forEach((store) => {
            console.log(store)
            storesHtml += `<li>${store}</li>`
        })
        document.querySelector('.content .stores_list').innerHTML =  storesHtml
      });
    }
  )
  .catch(function(err) {
    console.log('Fetch Error :-S', err);
  });