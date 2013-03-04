	var seriesOptions = [],
		yAxisOptions = [],
		seriesCounter = 0,
		colors = Highcharts.getOptions().colors;
	seriesOptions[0] = {
			name: "Median",
			data: medianPrices,
	        type: 'area',
		    shadow: true
		};
	seriesOptions[1] = {
			name: "Minimum",
			data: minimumPrices,
	        type: 'area',
		    shadow: true
		};
	seriesOptions[2] = {
			name: "Samples",
			data: sampleSizes,
	        type: 'area',
		    shadow: true,
		    yAxis: 1
		};
	/*
	seriesOptions[2] = {
			name: "Durchschnitt",
			data: averagePrices,
		    shadow: true
		};*/
$(function() {    window.chart = new Highcharts.StockChart({
	chart: {
		renderTo: 'highchart'
	},

	rangeSelector: {
		buttons: [{
			type: 'hour',
			count: 24,
			text: '24h'
		}, {
				type: 'day',
				count: 7,
				text: '1w'
		}, {
			type: 'all',
			count: 1,
			text: 'Alle'
		}],
		selected: 1,
		inputEnabled: false
	},

	title: {
		text: ''
	},

	xAxis: {
		maxZoom: 60 * 60 * 1000
	},

    tyAxis: yAxisOptions,

	yAxis: [{
		title: {
			text: 'Preis (Gold)'
		},
		min: 0,
        height: 200,
		lineWidth: 2
	}, {
        title: {
            text: 'Samples'
        },
        top: 300,
        height: 100,
        offset: 0,
        lineWidth: 2
    }],
	series: seriesOptions
});});