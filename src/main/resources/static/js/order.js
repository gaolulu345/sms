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
        qrcodeDialogVisible: false,

        totalCnt: null,
        currentPageSize: null,
        currentPageIndex: null,
        pageSizes: [10, 20, 50],

        dateRange: '',
        currentStartTime: '',
        currentEndTime: '',
        currentStatus: '',
        currentType: '',
        currentTerIds: [],
        currentOrderId: null,

        currentUserId: null,
        currentUserInfo: {},
        showUserInfoDialog: false,

        orderList: [],

        showLog: false,
        currentLogOrderId: null,
        logList: {},
        dialogPageSizes: [5, 10, 20],

        typeOptions: [{
                value: 0,
                label: '支付宝'
            },
            {
                value: 1,
                label: '微信钱包'
            },
            {
                value: 2,
                label: '测试'
            },
            {
                value: 3,
                label: '免费'
            },
            {
                value: 4,
                label: '商户一键启动'
            }, {
                value: 5,
                label: '维保一键启动'
            }, {
                value: 6,
                label: '线下支付(机器按钮)'
            }
        ],
        statusOptions: [{
            value: 0,
            label: '创建'
        }, {
            value: 1,
            label: '退款'
        }, {
            value: 2,
            label: '支付完成'
        }],
        statusList: ['创建', '退款', '支付完成'],
        typeList: ['支付宝', '微信钱宝', '测试', '免费', '商户一键启动', '维保一键启动', '线下支付(机器按钮)'],
        terOptions: [],
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
        dialogLabelWidth: '200px',
    },

    mounted: function () {
        console.log('mounted......', this.data)
        this.getTerList()
        this.getOrderList(10, 1, null, [], '', ' ', ' ', ' ')
    },

    methods: {
        getOrderList: function (pageSize, pageIndex, orderId, terIds, status, type, startTime, endTime) {
            console.log(terIds, this.data)
            let ids = terIds
            // let ids = terIds[0] ? terIds : []
            this.$http.post("/api/private/wash/order/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                orderId: orderId,
                status: status,
                type: type,
                terIds: ids,
                startTime: startTime,
                endTime: endTime
            }).then(function (res) {
                let data = res.json().data
                let result = data.result;
                if (result && result[0]) {
                    result.forEach(function (val) {
                        val.payTime = val.payTime ? formatTimestampToSecond(val.payTime) : '暂无'
                    })
                }

                vm.orderList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
                // vm.currentStatus = data.status;
                // vm.currentType = data.type;
                vm.currentStartTime = data.startTime;
                vm.currentEndTime = data.endTime;
                // vm.currentTerIds = data.terIds;

            })
        },

        getTerList: function () {
            this.$http.post("/api/private/wash/order/ter/selection", {}).then(function (res) {
                let result = res.json()
                vm.terOptions = result.data
            })
        },

        download: function () {
            let st = vm.currentStartTime ? vm.currentStartTime.split(' ')[0] : null
            let et = vm.currentEndTime ? vm.currentEndTime.split(' ')[0] : null
            let terId = vm.currentTerIds[0] || ''
            let status = vm.currentStatus || ''
            let type = vm.currentType || ''
            sTime = new Date(st);
            let today = new Date();
            let time = today - sTime;
            console.log(sTime, today, time);

            if (st && et) {
                if (time > 86400000 * 93) { //超过3个月
                    vm.$message.error('请选择近3个月的订单')
                } else {
                    if (vm.totalCnt > 0) {
                        window.location.href = "/api/private/wash/order/list/exprot?st=" + st + "&et=" + et + "&terId=" + terId + "&type=" + type + '&status=' + status;
                    } else {
                        vm.$message.error('无结果')
                    }
                }
            } else {
                vm.$message.error('请选择开始和结束日期！');
            }
        },

        // 查看用户信息
        getUserInfo: function (userId) {
            this.$http.post("/api/private/wash/user/list", {
                pageSize: 10,
                pageIndex: 1,
                id: userId,
                phone: null,
                startTime: '',
                endTime: ''
            }).then(function (res) {
                let data = res.json().data;
                let result = data.result;
                let genderList = ['未知', '男', '女']
                if (result && result[0]) {
                    let userInfo = result[0]
                    userInfo.gender = genderList[userInfo.gender]
                    userInfo.createTime = formatTimestampToSecond(userInfo.createTime)
                    userInfo.lastLoginTime = formatTimestampToSecond(userInfo.lastLoginTime)
                    vm.currentUserInfo = userInfo;
                    vm.showUserInfoDialog = true
                } else {
                    vm.$message.error('查无此用户')
                }
            })
        },

        // 查看日志
        getWashLog: function (orderId, pageIndex, pageSize) {
            vm.currentLogOrderId = orderId
            vm.$http.post("/api/private/wash/order/log/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                orderId: orderId
            }).then(function (res) {
                if (res.json().code == 200) {
                    let response = res.json().data
                    if (response.totalCnt > 0) {
                        response.result.forEach(function (val) {
                            val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                            val.valid = val.valid == false ? '不可用' : '空闲'
                        })
                        vm.logList = response
                        vm.showLog = true
                    } else {
                        vm.$message.error('暂无结果！')
                    }
                } else {
                    vm.$message.error(res.json().message)
                }
            })
        },
        // 日志分页操作
        logCurrentChange: function (val) {
            let orderId = vm.currentLogOrderId;
            let pageIndex = val;
            let pageSize = vm.logList.pageSize;
            vm.$http.post("/api/private/wash/order/log/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                orderId: orderId
            }).then(function (res) {
                let response = res.json().data
                response.result.forEach(function (val) {
                    val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                    val.valid = val.valid == false ? '不可用' : '空闲'
                })
                vm.logList = response
            })
        },
        logSizeChange: function (val) {
            let orderId = vm.currentLogOrderId;
            let pageIndex = 1;
            let pageSize = val;
            vm.$http.post("/api/private/wash/order/log/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                orderId: orderId
            }).then(function (res) {
                let response = res.json().data
                response.result.forEach(function (val) {
                    val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                    val.valid = val.valid == false ? '不可用' : '空闲'
                })
                vm.logList = response
            })
        },


        search: function () {
            if (!vm.dateRange) { //如果日期选择器中先有选择，后置空，结果为null，会报错
                vm.dateRange = '';
            }
            if (!vm.currentTerIds[0] || !vm.currentTerIds) {
                vm.currentTerIds = []
            }
            vm.currentStartTime = vm.dateRange[0] || '';
            vm.currentEndTime = vm.dateRange[1] || '';
            vm.getOrderList(vm.currentPageSize, 1, vm.currentOrderId, vm.currentTerIds, vm.currentStatus, vm.currentType, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            vm.getOrderList(val, 1, vm.currentOrderId, vm.currentTerIds, vm.currentStatus, vm.currentType, vm.currentStartTime, vm.currentEndTime);
        },
        handleCurrentChange(val) {
            vm.getOrderList(vm.currentPageSize, val, vm.currentOrderId, vm.currentTerIds, vm.currentStatus, vm.currentType, vm.currentStartTime, vm.currentEndTime);

        }
    }
});


$('.nav-each').removeClass('active');
$('.' + vm.pageName).addClass('active');