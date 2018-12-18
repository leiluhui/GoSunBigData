$(function () {

    var lineChart = echarts.init(document.getElementById("echarts-line-chart"));

    var lineoption = {
        title : {
            text: '最近7小时抓拍数量变化'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['人脸抓拍','行人抓拍','车辆抓拍']
        },
        grid:{
            x:40,
            x2:40,
            y2:24
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['00:00','01:00','02:00','03:00','04:00','05:00','06:00']
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 张'
                }
            }
        ],
        series : [
            {
                name:'人脸抓拍',
                type:'line',
                data:[11, 11, 15, 13, 12, 13, 10],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            },
            {
                name:'行人抓拍',
                type:'line',
                data:[11, 11, 15, 13, 12, 13, 10],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            },
            {
                name:'车辆抓拍',
                type:'line',
                data:[1, -2, 2, 5, 3, 2, 0],
                markPoint : {
                    data : [
                        {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };

    lineChart.setOption(lineoption);
    $(window).resize(lineChart.resize);
    // $.getJSON('/echarts/pie',function(lineoption){
    //     // lineoption = {
    //     //     title: {
    //     //         text: 'ECharts 入门示例'
    //     //     },
    //     //     tooltip : {
    //     //         trigger: 'axis'
    //     //     },
    //     //     legend: {
    //     //         data:['销量']
    //     //     },
    //     //     calculable : true,
    //     //     xAxis: {
    //     //         data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
    //     //     },
    //     //     yAxis: {data:null},
    //     //     series: [{
    //     //         name: '销量',
    //     //         type: 'pie',
    //     //         data: [5, 20, 36, 10, 10, 20]
    //     //     }]
    //     // };
    //
    //
    // })


    var todayPieChart = echarts.init(document.getElementById("echarts-today-pie"));
    var todaypieoption = {
            // title : {
            //     text: '今日抓拍百分比',
            //     x:'center'
            // },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} 张 ({d}%)"
            },
            legend: {
                bottom: '5px',
                data: ['人脸','行人','车辆']
            },
            series : [
                {
                    name: '抓拍来源',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:335, name:'人脸'},
                        {value:310, name:'行人'},
                        {value:234, name:'车辆'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

    todayPieChart.setOption(todaypieoption);

    window.onresize = todayPieChart.resize;


    var countPieChart = echarts.init(document.getElementById("echarts-count-pie"));
    var countpieoption = {
        // title : {
        //     text: '今日抓拍百分比',
        //     x:'center'
        // },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} 张 ({d}%)"
        },
        legend: {
            bottom: '5px',
            data: ['人脸','行人','车辆']
        },
        series : [
            {
                name: '抓拍来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:335, name:'人脸'},
                    {value:310, name:'行人'},
                    {value:234, name:'车辆'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    countPieChart.setOption(countpieoption);

    window.onresize = countPieChart.resize;

});
