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
        showCurrentImg: false,
        currentLogByImg: null,
        logList: [],

        totalCnt: null,
        currentPageSize: 10,
        currentPageIndex: 1,
        pageSizes: [10, 20, 50],

        currentOpSource: '',
        OpSourceOptions: [
            {
                label: '商户',
                value: 1
            },
            {
                label: '维保',
                value: 0
            }
        ],
        currentTerId: '',
        terList: [],
        searchTerOptions: [],
        searchTerLoading: false,
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
        dateRange: [],
        currentStartTime: '',
        currentEndTime: ''
    }, 

    mounted: function() {
        let paramData = {
            pageSize: this.$data.currentPageSize,
            pageIndex: this.$data.currentPageIndex
        }
        this.getLogList(paramData)
        this.getTerList()
    },

    computed: {
        computedDateRange: {
            get: function() {
                return this.dateRange
            },
            set: function(newValue) {
                if(newValue != null && newValue.length > 0) {
                    this.dateRange = newValue
                } else {
                    this.dateRange = ''
                }
                this.currentStartTime = vm.dateRange[0] || '';
                this.currentEndTime = vm.dateRange[1] || '';
            }
        }
    },
    
    methods: {
        getLogList: function(paramData){
            this.$http.post("/api/private/device/operation/log/list", 
                paramData
            ).then(function(res){
                let data = res.json().data
                // console.log('getLogList res: ',data)
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

        getTerList: function() {
            this.$http.post("/api/private/wash/ter/property/list/info", {
            }).then(function(res){
                let result = res.json().data.result
                if(result) {
                    result.forEach(function(item, index){
                        item['label'] = `${item.code}&${item.title}`
                        item['value'] = item.id.toString()
                    })
                    vm.terList = result
                    vm.searchTerOptions = result
                }
            })
        },

        searchTerByKeyword: function(queryString) {
            if (queryString != '') {
                let backupTerList = vm.terList
                vm.searchTerLoading = true
                vm.searchTerOptions = backupTerList.filter((ter) => {
                    return ter.label.indexOf(queryString) != -1
                })
                vm.searchTerLoading = false
            } else {
                vm.searchTerLoading = false
                vm.searchTerOptions = vm.terList
            }

        },

        search: function() {
            let paramData = {
                pageSize: vm.currentPageSize,
                pageIndex: vm.currentPageIndex
            }
            if(vm.currentOpSource !== '') {
                paramData.opSource = vm.currentOpSource
            }
            if(vm.currentTerId !== '') {
                paramData.terId = vm.currentTerId
            }
            if(vm.currentStartTime !== '' & vm.currentEndTime !== '') {
                paramData.startTime = vm.currentStartTime
                paramData.endTime = vm.currentEndTime
            }
            vm.getLogList(paramData)
        },

        handleSizeChange(val) {
            vm.currentPageSize = val
            vm.search();
        },

        handleCurrentChange(val) {
            vm.currentPageIndex = val
            vm.search();

        },

        showResetImg: function(val) {
            vm.showCurrentImg = true
            vm.currentLogByImg = val
            console.log('in showResetImg: ', vm.currentLogByImg)
        }
    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');