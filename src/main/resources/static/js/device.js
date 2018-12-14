var vm = new Vue({
    el: '#app',
    data: {
        pageName: 'device',
        pageTitle: '设备管理',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        qrcodeDialogVisible: false,
        deviceList: [],

        totalCnt: null,
        currentPageSize: 10,
        currentPageIndex: 1,
        pageSizes: [10, 20, 50],
    }, 

    mounted: function() {
        this.geDeviceList(this.$data.currentPageSize, this.$data.currentPageIndex)
    },
    methods: {
        geDeviceList: function(pageSize, pageIndex){
            this.$http.post("/api/private/wash/ter/property/all/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
            }).then(function(res){
                let data = res.json().data
                console.log(data)
                // let result = data.result;
                let result = data
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.screenSize = `${val.screenHigh}*${val.screenWide}`
                        val.adExistDesc = val.adExist ? '支持':'不支持'
                    })
                }
                vm.deviceList = result
                vm.totalCnt = data.totalCnt
            })
        },

        handleSizeChange(val) {
            vm.getLogList(val, 1);
        },

        handleCurrentChange(val) {
            vm.getLogList(vm.currentPageSize, val);

        },

        handlerDevice: function(val) {
            vm.currentDevice = val
            console.log('in showResetImg: ', vm.currentLogByImg)
        }
    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');