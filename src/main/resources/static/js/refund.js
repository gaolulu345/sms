

var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'refund',
        pageTitle: '退款列表',
    	admin: admin,
    	adminID: adminId,
        menuPer: menuPer,
        opPer: opPer,

        totalCnt: null,
        currentPageSize: null,
        currentPageIndex: null,
        pageSizes: [10, 20, 50],

        currentStatus: '',
        currentTerIds: [],

        refundList: [],

        reasonOptions: [{value: '0', label: '误购，请求退款'}, {value: '1', label: '设备无法启动'}, {value: '2', label: '洗车中途故障'}, {value: '3', label: '洗车服务不满意'}, {value: '4', label: '其他原因'}],
        statusOptions: [{value: '0', label: '默认状态'}, {value: '1', label: '请求退款'}, {value: '2', label: '请求通过审核'}, {value: '3', label: '已退款'}, {value: '4', label: '请求被拒绝'}],
            
        statusList: ['创建', '退款','支付完成']
        
    },

    mounted: function() {
        console.log('mounted......')
        this.getRefundList(10, 1)
        // this.getOrderList(10, 1, '', [], '', '')
    },

    methods: {
        // getOrderList: function(pageSize, pageIndex, status, terIds, startTime, endTime) {
        getRefundList: function(pageSize, pageIndex) {
            // console.log(terIds)
            // let ids = terIds[0] ? terIds : []
            this.$http.post("/api/private/refund/list", {
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
                    val.modifyTime = val.modifyTime ? formatTimestampToSecond(val.modifyTime) : '暂无'
                    val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                    val.checkTime = val.checkTime ? formatTimestampToSecond(val.checkTime) : '暂无'
                    val.refundTime = val.refundTime ? formatTimestampToSecond(val.refundTime) : '暂无'
                })
                vm.refundList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
        },



        search: function(){
            if(!vm.dateRange) {  //如果日期选择器中先有选择，后置空，结果为null，会报错
                vm.dateRange = '';
            }
            vm.currentStartTime = vm.dateRange[0] || '';
            vm.currentEndTime = vm.dateRange[1] || '';

            vm.getRefundList(vm.currentPageSize, 1, vm.currentStatus, vm.currentTerIds, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            vm.getRefundList(val, 1, vm.currentStatus, vm.currentTerIds, vm.currentStartTime, vm.currentEndTime);
        },
        handleCurrentChange(val) {
            vm.getRefundList(vm.currentPageSize, val, vm.currentStatus, vm.currentTerIds, vm.currentStartTime, vm.currentEndTime);
        }
    }
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');
