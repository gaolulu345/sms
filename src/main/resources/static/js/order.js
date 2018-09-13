

var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'order',
        pageTitle: '洗车订单',
    	admin: admin,
    	adminID: adminId,
        menuPer: menuPer,
        opPer: opPer,

        totalCnt: null,
        currentPageSize: null,
        currentPageIndex: null,
        pageSizes: [10, 20, 50],

        dateRange: '',
        currentStartTime: '',
        currentEndTime: '',
        currentStatus: '',
        currentTerIds: [],

        orderList: [],
    
        statusOptions: [{value: '0', label: '创建'}, {value: '1', label: '退款'}, {value: '2', label: '支付完成'}],
        statusList: ['创建', '退款','支付完成'],
        terOptions: [],
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
    },

    mounted: function() {
        console.log('mounted......')
        this.getOrderList(10, 1)
        // this.getOrderList(10, 1, '', [], '', '')
        this.getTerList()
    },

    methods: {
        // getOrderList: function(pageSize, pageIndex, status, terIds, startTime, endTime) {
        getOrderList: function(pageSize, pageIndex) {
            // console.log(terIds)
            // let ids = terIds[0] ? terIds : []
            this.$http.post("/api/private/order/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                // status: status,
                // terIds: ids,
                // startTime: startTime,
                // endTime: endTime
            }).then(function(res){
                let data = res.json().data
                let result = data.result;
                result.forEach(function(val) {
                    val.payTime = val.payTime ? formatTimestampToSecond(val.payTime) : '暂无'
                })
                vm.orderList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
        },

        // getTerList: function() {
        //     this.$http.post("/api/private/partner/ter/order/selection", {}).then(function(res){
        //         let result = res.json()
        //         vm.terOptions = result.data
        //     })
        // },

        search: function(){
            if(!vm.dateRange) {  //如果日期选择器中先有选择，后置空，结果为null，会报错
                vm.dateRange = '';
            }
            vm.currentStartTime = vm.dateRange[0] || '';
            vm.currentEndTime = vm.dateRange[1] || '';

            vm.getOrderList(vm.currentPageSize, 1, vm.currentStatus, vm.currentTerIds, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            vm.getOrderList(val, 1, vm.currentStatus, vm.currentTerIds, vm.currentStartTime, vm.currentEndTime);
        },
        handleCurrentChange(val) {
            vm.getOrderList(vm.currentPageSize, val, vm.currentStatus, vm.currentTerIds, vm.currentStartTime, vm.currentEndTime);
        }
    }
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');
