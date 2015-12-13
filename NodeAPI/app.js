//Lets require/import the HTTP module
var http = require('http');
var dispatcher = require('httpdispatcher');
var jsdom = require('jsdom');
var https = require('https');
var request = require('superagent');
var fs= require('fs');
var async = require('async');

//Lets define a port we want to listen to
const PORT=8080; 

dispatcher.setStatic('resources');

dispatcher.onGet("/blackrock/year", function(req, res) {
  request
  .get('https://test3.blackrock.com/tools/hackathon/security-data?identifiers=GS')
  .end(function(err, result){
    res.writeHead(200, {'Content-Type': 'application/json'});
    var resultMap = result.body.resultMap;
    res.end(JSON.stringify(resultMap));
  });
}); 

dispatcher.onGet("/country/means", function(req, res) {
  var means = batchSendRequest(function(means) {
    res.writeHead(200, {'Content-Type': 'application/json'});
    res.end(JSON.stringify(means));
  });
}); 

function batchSendRequest(callback) {
  var identifiers = getIdentifiers().split(",");
  var means = {};
  var countryCount = {};
  for(var i = 0; i < identifiers.length; i++) {
    var country = getCountry(identifiers[i], function(result) {
      if(result == undefined)
        result = 'United States';
      if(result.constructor == Array){
        result = 'United States';
      }
      if(result in countryCount) {
        countryCount[result] += 1;
      } else {
        countryCount[result] = 1;
      }
      var price = getPrice(identifiers[i], function(resultA) {
        if(result in means) {
          means[result] *= ((means[result] * countryCount[result] - 1) + resultA)/countryCount[result];
        } else {
          means[result] = resultA;
        }
      });
    });
  }
  callback(means);
}

function getCountry(identifier) {
  var url = "https://test3.blackrock.com/tools/hackathon/security-data?identifiers=" + identifier;
  request
  .get(url)
  .end(function(err, result){
    if(result != undefined) {
        var country;
        try{
          country = result.body.resultMap.SECURITY[0].country;
          if(country == undefined) {
              country = [];
              var countries = result.body.resultMap.SECURITY[0].breakdowns.country[0];
              for(var i = 0; i < countries.length; i++) {
                country.push(countries.name);
              }
              if(country == [])
                country = 'United States';
          }
      } catch(err) {
        country = 'United States';
      }
      return country;
    }
  });
}

function getPrice(identifier) {
  var url = "https://test3.blackrock.com/tools/hackathon/performance?identifiers=" + identifier;
  var price;
  request
  .get(url)
  .end(function(err, result){
      var resultMap;
      if(result != undefined) {
        try{
          resultMap = result.body.resultMap.RETURNS['0'].performanceChart;
          return resultMap[resultMap.length-1][1];
        } catch(err){

        }
      }
  });
}

function getIdentifiers() {
  var content = fs.readFileSync('identifiers.txt', 'utf8');
  return content;
}

var identifiers = getIdentifiers();
for(var i = 0; i < identifiers.length; i++){
  var country = function(){
    return getCountry(identifiers[i]);
  }
  console.log(country());
  async.series({
    country:   function(){return getCountry(identifiers[i])},
    price:  function(){return getPrice(identifiers[i])}
    }, function(err, result) {
      console.log("Country: " + result.country + ", Price: " + result.price);
    });
}
//Lets use our dispatcher
function handleRequest(request, response){
    try {
        //log the request on console
        console.log(request.url);
        //Disptach
        dispatcher.dispatch(request, response);
    } catch(err) {
        console.log(err);
    }
}

//Create a server
var server = http.createServer(handleRequest);

//Lets start our server
server.listen(PORT, function(){
    //Callback triggered when server is successfully listening. Hurray!
    console.log("Server listening on: http://localhost:%s", PORT);
});