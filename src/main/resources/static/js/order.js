

var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'order',
        pageTitle: '洗车订单',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,

        totalCnt: null,
        currentPageSize: null,
        currentPageIndex: null,
        pageSizes: [10, 20, 50],

        dateRange: '',
        currentStartTime: '',
        currentEndTime:'',
        currentStatus: '',
        currentType: '',
        currentTerIds: [],

        orderList: [],
    
        typeOptions: [{value: 0, label: '支付宝'}, {value: 1, label: '微信钱包'}, {value: 2, label: '测试'}, {value: 3, label: '免费'}],
        statusOptions: [{value: 0, label: '创建'}, {value: 1, label: '退款'}, {value: 2, label: '支付完成'}],
        statusList: ['创建', '退款','支付完成'],
        typeList: ['支付宝', '微信钱宝', '测试', '免费'],
        terOptions: [],
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
    },

    mounted: function() {
        console.log('mounted......')
        this.getOrderList(10, 1, [], '', ' ', ' ', ' ')
        // this.getOrderList(10, 1, '', [], '', '')
        this.getTerList()
    },

    methods: {
        // getOrderList: function(pageSize, pageIndex, status, terIds, startTime, endTime) {
        getOrderList: function(pageSize, pageIndex, terIds, status, type, startTime, endTime) {
            console.log(terIds)
            let ids = terIds[0] ? terIds : []
            this.$http.post("/api/private/order/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                status: status,
                type: type,
                terIds: ids,
                startTime: startTime,
                endTime: endTime
            }).then(function(res){
                let data = res.json().data
                let result = data.result;
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.payTime = val.payTime ? formatTimestampToSecond(val.payTime) : '暂无'
                    })
                }
                
                vm.orderList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
                vm.currentStatus = data.status;
                vm.currentType = data.type;
                vm.currentStartTime = data.startTime;
                vm.currentEndTime = data.endTime;
                vm.currentTerIds = data.terIds;
                
            })
        },

        getTerList: function() {
            this.$http.post("/api/private/order/ter/selection", {}).then(function(res){
                let result = res.json()
                vm.terOptions = result.data
            })
        },

        download: function(){
            let st = vm.currentStartTime
            let et = vm.currentEndTime
            sTime = new Date(st);
            let today = new Date();
            let time = today - sTime;
            console.log(sTime, today, time);

            if(st && et) {
                if (time > 86400000*93) { //超过3个月
                    vm.$message.error('请选择近3个月的订单')
                } else {
                   if (vm.totalCnt > 0) {
                        window.location.href = "/api/private/order/list/exprot?st=" + st + "&et=" + et + "&terId=" + vm.currentTerIds[0] + "&type=" + vm.currentType;
                    } else {
                        vm.$message.error('无结果')
                    }
                }
            } else {
                vm.$message.error('请选择开始和结束日期！');
            }
        },


        search: function(){
            if(!vm.dateRange) {  //如果日期选择器中先有选择，后置空，结果为null，会报错
                vm.dateRange = '';
            }
            vm.currentStartTime = vm.dateRange[0] || '';
            vm.currentEndTime = vm.dateRange[1] || '';
            vm.getOrderList(vm.currentPageSize, 1, vm.currentTerIds, vm.currentStatus, vm.currentType, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            vm.getOrderList(val, 1, vm.currentTerIds, vm.currentStatus, vm.currentType, vm.currentStartTime, vm.currentEndTime);
        },
        handleCurrentChange(val) {
            vm.getOrderList(vm.currentPageSize, val, vm.currentTerIds, vm.currentStatus, vm.currentType, vm.currentStartTime, vm.currentEndTime);

        }
    }
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');
