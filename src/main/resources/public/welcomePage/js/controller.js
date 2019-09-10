
 var clicked=   async $("addBtn").click(function(event){
               event.preventDefault();
              await axios.get( "apps/method3?number1="+document.getElementById('number1').value+"&number2="+document.getElementById('number2').value);
               .then(function (response) {
                    orders=response.data
                    console.log(orders)
               })
               .catch(function (error) {
                 console.log('There is a problem with our servers. We apologize for the inconvince, please try again later', error.message);

               })
               .then(function () {
                 // always executed
               });

        }
