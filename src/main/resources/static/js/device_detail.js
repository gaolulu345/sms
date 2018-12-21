var vm = new Vue({
    el: '#app',
    data: {
        pageName: 'deviceDetail',
        pageTitle: '设备详情',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        qrcodeDialogVisible: false,

        deviceId: null,
        device: null,
        deviceCarouselQuery: null,
        backupDevice: null,
        terOptions: null,
        deleteCarousels: null,
        terClientVersionOptions: [
            {
                label: 'Java',
                value: 'Java'
            },
            {
                label: 'Python',
                value: 'Python'
            },
        ],

        netMethodOptions: [
            {
                label: '有线宽带',
                value: 0
            },
            {
                label: '无线宽带',
                value: 1
            },
        ],

        terBusiModeOptions: [
            {
                label: '单网点无人值守',
                value: 0
            },
            {
                label: '单网点有人值守',
                value: 1
            },
            {
                label: '代理模式一',
                value: 2
            },
            {
                label: '代理模式二',
                value: 3
            }
        ],

        videoControlOptions: [
            {
                label: '海康威视',
                value: '海康威视'
            },
            {
                label: '乐橙',
                value: '乐橙'
            },
        ]

    }, 

    mounted: function() {
        let href = window.location.href
        if (href.indexOf('?') != -1) {
            let query = href.split('?')[1].split('&')
            let param = {}
            query.forEach(function(item, index){
                let itemArrey = item.split('=')
                param[itemArrey[0]] = itemArrey[1]
            })
            this.deviceId = param.id
        }
        this.getDeviceDetail(this.deviceId)
    },
    methods: {

        // 设备详情
        getDeviceDetail: function(decviceId){
            console.log('id',decviceId)
            this.$http.post("/api/private/wash/ter/property/search", {
                id: decviceId,
            }).then(function(res){
                let result = res.json()
                if(result.code == 200) {
                    result = result.data
                    if(result) {
                        result.screenSize = `${result.screenHigh}*${result.screenWide}`
                        result.adExistDesc = result.adExist ? '支持':'不支持'
                        result.screen = null
                        vm.device = [result]
                        let backupDevice = {}
                        Object.keys(result).forEach(function(item, index){
                            backupDevice[item] = {
                                value: result[item],
                                edit: false,
                                cdrPictureDialogVisible: false
                            }
                        })
                        console.log('out getDeviceCarouselQuery')
                        vm.backupDevice = backupDevice
                        if (vm.device[0].terBusiMode != null) {
                            console.log('in getDeviceCarouselQuery')
                            vm.getDeviceCarouselQuery(vm.device[0].id)
                        }
                    }
                    console.log('vm.backupDevice: ', vm.backupDevice)
                }
            })
        },

        // 保存更改
        saveDevice: function(paramKey) {
            let updateDevice = {}
            let updatefield = {}
            console.log('保存更改vm.backupDevice: ', vm.backupDevice)
            if (paramKey == 'screen') {
                updatefield['screenHigh'] = vm.backupDevice.screenHigh.value
                updatefield['screenWide'] = vm.backupDevice.screenWide.value
            }else{
                updatefield[paramKey] = vm.backupDevice[paramKey].value
            }
            console.log('updatefield：', updatefield, paramKey)
            Object.keys(updatefield).forEach(function(item, index){
                updateDevice[item] = vm.backupDevice[item].value
            })
            updateDevice['id'] = vm.backupDevice.id.value
            console.log('待升级字段：', updateDevice, paramKey)
            this.updateDevice(updateDevice, paramKey)
        },

        // 取消
        cancel: function(paramKey) {
            vm.backupDevice[paramKey].edit = false
        },

        // 获取网点列表
        terQuerySearch: function(queryString, callback) {
            vm.backupDevice.terId.edit = true
            this.$http.post("/api/private/wash/ter/property/list/info", {
            }).then(function(res){
                let result = res.json().data.result
                if(result) {
                    result.forEach(function(item, index){
                        item['label'] = `${item.code}&${item.title}`
                        item['value'] = item.id.toString()
                    })
                    vm.terOptions = result
                }
            })
        },

        // 获取轮播图列表
        getDeviceCarouselQuery: function(deviceId) {
            console.log('deviceId getDeviceCarouselQuery ID', deviceId)
            this.$http.post("/api/private/ter/ratation/picture/show", {
                "deviceId": deviceId,
	            "pageSize": 10,
                "pageIndex": 1
            }).then(function(res){
                let result = res.json()
                console.log('getDeviceCarouselQuery result: ', result)
                if(result.code == 200) {
                    let resData = result.data.result
                    if(resData) {
                        let enableCarouselQuery = []
                        resData.forEach(function(item, index) {
                            if (!item.deleted) {
                                enableCarouselQuery.push(item)
                            }
                        })
                        vm.deviceCarouselQuery = enableCarouselQuery
                    }
                }    
            })
        },

        // 更改广告支持
        adExistChange: function() {
            let updateDevice = {}
            updateDevice['adExist'] = vm.backupDevice.adExist.value
            updateDevice['id'] = vm.backupDevice.id.value
            this.updateDevice(updateDevice, 'adExist')
        },

        updateDevice: function(updateDevice, paramKey) {
            this.$http.post("/api/private/wash/ter/property/info/update", updateDevice).then(
                function(res){
                    let result = res.json()
                    if(result.code == 200) {
                        this.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        vm.getDeviceDetail(vm.deviceId)
                    } else {
                        this.$message.error(result.message);
                        vm.backupDevice[paramKey].edit = falsefalse
                    }

                },
                function(res){
                    this.$message.error('保存失败');
                    vm.backupDevice[paramKey].edit = false
                }
            )
        },

        // 视频服务提示
        videoControlQuerySearch: function(queryString, cb) {
            cb(vm.videoControlOptions);
        },

        // 选择待删除轮播图
        changeDeleteCarousel: function(val) {
            if (val && val.length > 0) {
                let deleteCarousels = []
                val.forEach(function(item, index){
                    deleteCarousels.push(item.id)
                })
                vm.deleteCarousels = deleteCarousels
            }
        },

        // 批量删除轮播图
        deleteDeviceCarousel: function() {
            let ids = vm.deleteCarousels
            if (ids && ids.length > 0){
                let idStr = ids.join(', ')
                this.$confirm(`此操作将删除ID为[${idStr}]的轮播图, 是否继续?`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    center: true
                    }).then(() => {
                        vm.sendDeleteDeviceCarousel({
                            ids: ids,
                            deleted: true
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
            } else {
                this.$message.error('请选择至少一张轮播图');
            }
        },

        // 删除请求
        sendDeleteDeviceCarousel: function(data) {
            this.$http.post("/api/private/ter/ratation/picture/appoint/delete", data
            ).then(
                function(res){
                    let result = res.json()
                    if(result.code == 200) {
                        this.$message({
                            message: '批量删除成功',
                            type: 'success'
                        });
                        vm.getDeviceCarouselQuery(vm.deviceId)
                    } else {
                        this.$message.error(result.message);
                    }

                },
                function(res){
                    this.$message.error('批量删除失败');
                }
            )
        },

        // // 编辑备注
        // editPropertyRemark: function() {
        //     let inputPlaceholder = vm.device[0].propertyRemark
        //     this.$prompt('请输入设备备注', '提示', {
        //         confirmButtonText: '确定',
        //         cancelButtonText: '取消',
        //         inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
        //         inputErrorMessage: '邮箱格式不正确',
        //         inputPlaceholder: inputPlaceholder
        //       }).then(({ value }) => {
        //         this.$message({
        //           type: 'success',
        //           message: '你的邮箱是: ' + value
        //         });
        //       }).catch(() => {
        //         this.$message({
        //           type: 'info',
        //           message: '取消输入'
        //         });       
        //       });
        // }

    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');