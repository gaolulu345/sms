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

        currentOpSource: null,
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
        currentTerIds: [],
        terList: [],
        searchTerOptions: [],
        searchTerLoading: false,
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
        dateRange = null,
        currentStartTime = null,
        currentEndTime = null
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
                return vm.dateRange
            },
            set: function(newValue) {
                if(!newValue) {  //如果日期选择器中先有选择，后置空，结果为null，会报错
                    vm.dateRange = ''
                } else {
                    vm.dateRange = newValue
                }
                vm.currentStartTime = vm.dateRange[0] || '';
                vm.currentEndTime = vm.dateRange[1] || '';
            }
        }
    },
    
    methods: {
        getLogList: function(paramData){
            this.$http.post("/api/private/device/operation/log/list", 
                paramData
            ).then(function(res){
                let data = res.json().data
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
                    VM.searchTerOptions = result
                }
            })
        },

        searchTerByKeyword: function(queryString) {
            console.log('searchTerByKeyword: ', queryString)
            if (queryString != '') {
                let backupTerList = vm.terList
                vm.searchTerLoading = true
                vm.searchTerOptions = backupTerList.filter((ter) => {
                    return ter.label.indexOf(queryString) != -1
                })
                vm.searchTerLoading = false
            } else {
                vm.searchTerLoading = false
                vm.searchTerLoading = vm.terList
            }

        },

        search: function() {
            //  getTerList





            
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