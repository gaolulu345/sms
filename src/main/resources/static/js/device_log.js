var vm = new Vue({
    el: '#app',
    data: {
        pageName: 'deviceLog',
        pageTitle: '设备日志管理',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        qrcodeDialogVisible: false,
        logList: [],

        totalCnt: null,
        currentPageSize: 10,
        currentPageIndex: 1,
        pageSizes: [10, 20, 50],
    }, 

    mounted: function() {
        this.getLogList(this.$data.currentPageSize, this.$data.currentPageIndex)
    },
    methods: {
        getLogList: function(pageSize, pageIndex){
            this.$http.post("/api/private/device/operation/log/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
            }).then(function(res){
                let data = res.json().data
                console.log(data)
                let result = data.result;
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                        val.handlerResult = val.success ? '操作成功':'操作失败'
                    })
                }
                vm.logList = result
                vm.totalCnt = data.totalCnt
            })
        },

        showMore: function() {
            vm.more = true
            this.getLogList(true)
        },

        handleSizeChange(val) {
            vm.getLogList(val, 1);
        },

        handleCurrentChange(val) {
            vm.getLogList(vm.currentPageSize, val);

        }
    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');