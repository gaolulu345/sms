var vm = new Vue({
    el: '#app',
    data: {
        pageName: 'device',
        pageTitle: '设备列表',
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

        dialogDeviceFormVisible: false,
        deviceForm: {
            deviceType: null,
            highLimit: null,
            wideLimit: null,
            bubbleLimit: null
        },
        deviceBusiModeOptions: [
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

        deviceFormRules: {
            deviceType: [
              { type:'number', required: true, message: '请选择设备类型', trigger: 'blur' }
            ],
            highLimit: [
              { type:'number', required: true, message: '请输入设备限高', trigger: 'blur' }
            ],
            wideLimit: [
              { type:'number', required: true, message: '请输入设备限宽', trigger: 'blur' }
            ],
            bubbleLimit: [
              { type:'number', required: true, message: '请输入设备泡沫使用次数', trigger: 'blur' }
            ]
          }
    }, 

    mounted: function() {
        this.getDeviceList(this.$data.currentPageSize, this.$data.currentPageIndex)
    },
    methods: {
        getDeviceList: function(pageSize, pageIndex){
            this.$http.post("/api/private/wash/ter/property/all/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
            }).then(function(res){
                let data = res.json().data
                // console.log('res: ', data)
                let result = data.result;
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.screenSize = `${val.screenHigh}*${val.screenWide}`
                        val.adExistDesc = val.adExist ? '支持':'不支持'
                        val.frpIp = val.frpIp === null ? '':val.frpIp
                        val.frpPort = val.frpPort === null ? '':val.frpPort
                        val.frp = val.frpIp || val.frpPort ? val.frpIp + ":" + val.frpPort:null
                    })
                }
                vm.deviceList = result
                vm.totalCnt = data.totalCnt
                console.log('vm.deviceList: ', vm.deviceList)
            })
        },

        downloadDeviceList: function() {
            window.location.href = "/api/private/wash/ter/property/list/exprot"
        },

        handleSizeChange(val) {
            vm.getDeviceList(val, 1);
        },

        handleCurrentChange(val) {
            vm.getDeviceList(vm.currentPageSize, val);

        },

        handlerDevice: function(val) {
            vm.currentDevice = val
            this.toDeviceDetail(val.id)
        },

        // 管理设备
        toDeviceDetail: function(id) {
            window.open('/pages/detail?id=' + id, "_blank");
        },

        // 删除设备
        deleteDevice: function(val) {
            this.$confirm(`确定删除id为${val.id}的设备？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                 vm.sendDeleteDevice(val.id)
              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消'
                });          
              });
        },

        sendDeleteDevice: function(id) {
            this.$http.post("/api/private/wash/ter/property/update/deleted", {
                id: id,
                deleted: true,
            }).then(
                function(res){
                    let result = res.json()
                    if(result.code == 200) {
                        this.$message({
                            message: '已删除',
                            type: 'success'
                        });
                        vm.getDeviceList(10, 1)
                    } else {
                        this.$message({
                            message: `${result.message}`,
                            type: 'warning'
                        });
                    }
                },
                function(res){
                    this.$message({
                        message: '删除失败',
                        type: 'warning'
                    });
                }
            )
        },

        // 创建设备
        submitDeviceForm: function(formName) {
            this.$refs[formName].validate((valid) => {
              if (valid) {
                vm.sendCreateDevice(vm.deviceForm)
              }
            });
          },
        
        // 重置新建设备表单
        resetDeviceForm: function(formName) {
            this.$refs[formName].resetFields();
        },

        // 发送创建设备
        sendCreateDevice: function(data) {
            this.$http.post("/api/private/wash/ter/property/insert", data).then(
                function(res){
                    let result = res.json()
                    if(result.code == 200) {
                        this.$message({
                            message: '创建成功',
                            type: 'success'
                        });
                        vm.dialogDeviceFormVisible = false
                        vm.resetDeviceForm('deviceForm')
                        vm.getDeviceList(10, 1)
                    } else {
                        this.$message({
                            message: `${result.message}`,
                            type: 'warning'
                        });
                    }
                },
                function(res){
                    this.$message({
                        message: '创建失败',
                        type: 'warning'
                    });
                }
            )
        }
    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');