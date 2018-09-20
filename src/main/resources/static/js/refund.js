

var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'refund',
        pageTitle: '洗车退款',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,

        totalCnt: null,
        currentPageSize: null,
        currentPageIndex: null,
        pageSizes: [10, 20, 50],

        currentStatus: null,
        currentReason: null,
        currentOrderId: null,
        dateRange: null,
        currentStartTime: null,
        currentEndTime: null,

        refundList: [],

        reasonOptions: [{value: 0, label: '误购，请求退款'}, {value: 1, label: '设备无法启动'}, {value: 2, label: '洗车中途故障'}, {value: 3, label: '洗车服务不满意'}, {value: 4, label: '其他原因'}],
        statusOptions: [{value: 0, label: '默认状态'}, {value: 1, label: '请求退款'}, {value: 2, label: '请求通过审核'}, {value: 3, label: '已退款'}, {value: 4, label: '请求被拒绝'}],
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
    },

    mounted: function() {
        console.log('mounted......')
        this.getRefundList(10, 1, null, null, null, null, null)
    },

    methods: {
        getRefundList: function(pageSize, pageIndex, status, reason, orderId, startTime, endTime) {
            this.$http.post("/api/private/refund/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                status: status,
                reason: reason,
                orderId: orderId,
                startTime: startTime,
                endTime: endTime
            }).then(function(res){
                let data = res.json().data
                let result = data.result;
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.modifyTime = val.modifyTime ? formatTimestampToSecond(val.modifyTime) : '暂无'
                        val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                        val.checkTime = val.checkTime ? formatTimestampToSecond(val.checkTime) : '暂无'
                        val.refundTime = val.refundTime ? formatTimestampToSecond(val.refundTime) : '暂无'
                    })
                }
                
                vm.refundList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
                vm.currentStatus = data.status;
                vm.currentReason = data.reason;
                vm.currentOrderId = data.orderId;
                vm.currentStartTime = data.startTime;
                vm.currentEndTime = data.endTime;
            })
        },

        check: function(refundId, status) {
            this.$http.post("/api/private/refund/approved", {
                id: refundId,
                status: status
            }).then(function(res){
                let result = res.json()
                if(result.code == 200){
                    vm.getRefundList(vm.currentPageSize, vm.currentPageIndex, vm.currentStatus, vm.currentReason, vm.currentOrderId, vm.currentStartTime, vm.currentEndTime);
                } else {
                    vm.$message.error('操作失败！');
                }
            })
        },

        payback: function(refundId) {
            this.$http.post("/api/private/refund/back", {
                id: refundId
            }).then(function(res){
                let result = res.json()
                if(result.code == 200){
                    vm.getRefundList(vm.currentPageSize, vm.currentPageIndex, vm.currentStatus, vm.currentReason, vm.currentOrderId, vm.currentStartTime, vm.currentEndTime);
                } else {
                    vm.$message.error('退款失败！');
                }
            })
        },



        // 下载
        download: function(){
            // let orderId = vm.currentOrderId == null ? '' : vm.currentOrderId ;
            let reason = vm.currentReason == null ? '' : vm.currentReason;
            let status = vm.currentStatus == null ? '' : vm.currentStatus;

            let st = vm.currentStartTime ? vm.currentStartTime.split(' ')[0] : null
            let et = vm.currentEndTime ? vm.currentEndTime.split(' ')[0] : null
            sTime = new Date(st);
            let today = new Date();
            let time = today - sTime;
            console.log(sTime, today, time);

            if(st && et) {
                if (time > 86400000*93) { //超过3个月
                    vm.$message.error('请选择近3个月的退款列表')
                } else {
                    if (vm.totalCnt > 0) {
                        // window.location.href = '/api/private/refund/list/exprot?st=' + st + '&et=' + et + '&reason=' + '' + '&status=' + '';
                        window.location.href = '/api/private/refund/list/exprot?st=' + st + '&et=' + et + '&reason=' + reason + '&status=' + status;
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
                vm.dateRange = null;
                vm.currentStartTime = null;
                vm.currentEndTime = null;
            } else {
                vm.currentStartTime = vm.dateRange[0] || null;
                vm.currentEndTime = vm.dateRange[1] || null;
            }
            
            vm.getRefundList(vm.currentPageSize, 1, vm.currentStatus, vm.currentReason, vm.currentOrderId, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            vm.getRefundList(val, 1, vm.currentStatus, vm.currentReason, vm.currentOrderId, vm.currentStartTime, vm.currentEndTime);
        },
        handleCurrentChange(val) {
            vm.getRefundList(vm.currentPageSize, val, vm.currentStatus, vm.currentReason, vm.currentOrderId, vm.currentStartTime, vm.currentEndTime);
        }
    }
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');
