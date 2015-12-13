$(function () {

    // Prepare demo data
    var exampleData = [
        {
            "hc-key": "us",
            "value": 2
        },
        {
            "hc-key": "jp",
            "value": 3
        },
        {
            "hc-key": "fr",
            "value": 6
        },
        {
            "hc-key": "de",
            "value": 111
        },
        {
            "hc-key": "ru",
            "value": 71
        },
        {
            "hc-key": "cn",
            "value": 8
        },
        {
            "hc-key": "gb",
            "value": 20
        },
        {
            "hc-key": "it",
            "value": 63
        },
        {
            "hc-key": "ca",
            "value": 29
        },
        {
            "hc-key": "il",
            "value": 183
        }
    ];

    // Initiate the chart
    $('#container').highcharts('Map', {

        title : {
            text : 'Stocks etc'
        },

        subtitle : {
            text : 'some subtitle'
        },

        mapNavigation: {
            enabled: true,
            buttonOptions: {
                verticalAlign: 'bottom'
            }
        },

        colorAxis: {
            min: 0,
            minColor: "#E35D1B",
            maxColor: "#3DE31B",
        },
        tooltip: {
            /* http://api.highcharts.com/highmaps#tooltip */
            formatter: function() {
                return "Country: " + this.point.name + "<br/> Average stock value: " + this.point.value;
            }
        },
        series : [{
            data : exampleData,
            mapData: Highcharts.maps['custom/world'],
            joinBy: 'hc-key',
            name: 'Stock value',
            states: {
                hover: {
                    color: '#1BA1E3'
                }
            },
            dataLabels: {
                enabled: false,
                // format: '{point.name}'
            }
        }]
    });
    
    function updateData(data) {
        var chart = $('#container').highcharts();
        // console.log("setting series data");
        // console.log(data);
        chart.series[0].setData(data);
        chart.redraw();
    };

    function getData(value, callback) {
        /* asynchronously get some data here with given *value* of slider */

        //this is just some mock shit
        var myData = [
            {
                "hc-key": "us",
                "value": 2
            },
            {
                "hc-key": "jp",
                "value": 3
            },
            {
                "hc-key": "fr",
                "value": 6
            },
            {
                "hc-key": "de",
                "value": 111
            },
            {
                "hc-key": "ru",
                "value": 71
            },
            {
                "hc-key": "cn",
                "value": 8
            },
            {
                "hc-key": "gb",
                "value": 20
            },
            {
                "hc-key": "it",
                "value": 63
            },
            {
                "hc-key": "ca",
                "value": 29
            },
            {
                "hc-key": "il",
                "value": 183
            }
        ];
        for (i=0; i< myData.length; i++) {
            myData[i].value= Math.random()*200 + 50;
        }
        /*myData.push({
            "hc-key": "pl",
            "value": 8
        })*/
        //call callback on this newly retrieved data
        callback(myData);
    }
    var slider = $('#slider').slider({
      formatter: function(value) {
        /*getData(value, function(data) {
            updateData(data);
        })*/
        return 'Current value: ' + value;
      }
    });
    slider.on('slide', function(data) {
        getData(data.value, function(mapData){
            updateData(mapData);
        })
        // console.log(data.value);
    })

    /*$('#input').click(function onClick(data) {
        updateData("sticng");
        // alert(data);   
        // return this;
    });*/
    /*$('#button').click(function () {
        var chart = $('#container').highcharts();
        chart.series[0].setData([129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4, 29.9, 71.5, 106.4]);
    });*/
});

