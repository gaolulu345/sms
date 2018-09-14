

var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'upload',
        pageTitle: '文件管理',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,

        totalCnt: null,
        currentPageSize: null,
        currentPageIndex: null,
        pageSizes: [5, 10, 20, 50],

        currentAdminName: '',
        currentFileKey: '',

        dateRange: '',
        currentStartTime: '',
        currentEndTime: '',

        uploadList: [],
        uploadSelection: [],
        fileList: [],
        fileCnt: 0,
    
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },
    },

    mounted: function() {
        console.log('mounted......')
        this.getUploadList(5, 1, '', '', '', '')
    },

    methods: {
        getUploadList: function(pageSize, pageIndex, adminName, fileKey, startTime, endTime) {
            this.$http.post("/api/private/file/upload/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                adminName: adminName,
                fileKey: fileKey,
                startTime: startTime,
                endTime: endTime
            }).then(function(res){
                let data = res.json().data
                let result = data.result;
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                    })
                }
                
                vm.uploadList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
                vm.currentAdminName = data.adminName;
                vm.currentFileKey = data.fileKey;
                vm.currentStartTime = data.startTime;
                vm.currentEndTime = data.endTime;
            })
        },

        // 上传
        uploadSuccess: function(res, file, fileList){
            console.log(res, fileList.length);
            vm.fileCnt ++;
            console.log('这是第几个啦? ',vm.fileCnt)
            if(res.code == 200) {
                this.$message.success('上传成功');
                vm.fileCnt == fileList.length ? window.location.reload() : '';
            } else {
                if(res.message == 'NoFileFound') {
                    this.$message.warning('无上传文件或选择了空文件');
                } else if(res.message == 'FileToUpload') {
                    this.$message.warning('上传至阿里云失败');
                } else if(res.message == 'FileSizeOver') {
                    this.$message.warning('文件超过2M');
                } else if(res.message == 'NoAdminName') {
                    this.$message.warning('登录超时，取不到用户名');
                    window.location.href = '/login';
                } else {
                    this.$message.warning('上传至阿里云失败!');
                }
                vm.fileCnt == fileList.length ? window.location.reload() : '';
            }
        },
        handleExceed: function(files, fileList) {
            this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件`);
        },
        beforeUpload: function(file) {
            const isIMG = (file.type == 'image/jpeg' || file.type == 'image/gif' || file.type == 'image/png');
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isIMG) {
                this.$message.error('上传图片只能是 JPG、PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传文件大小不能超过 2M!');
            }
            return isIMG && isLt2M;
        },

        // 批量删除
        handleUploadSelectionChange(val) {
            this.uploadSelection = val;
        },
        deleteUpload: function() {
            if (vm.uploadSelection.length == 0) {
                vm.$message.warning('请选择要删除的图片！');
            } else {
                let selection = vm.uploadSelection
                let ids = []
                for(var key in selection) {
                    ids.push(selection[key].id)
                }
                console.log(ids)

                vm.$confirm('确认删除这' + ids.length + '个图片?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.$http.post("/api/private/file/delete", {
                        ids: ids
                    }).then(function(res){
                        let result = res.json();
                        if(result.code == 200){
                            vm.$message.success('已删除');
                            vm.getUploadList(vm.currentPageSize, vm.currentPageIndex, vm.currentAdminName, vm.currentFileKey, vm.currentStartTime, vm.currentEndTime);
                        }else {
                            vm.$message.error(result.message)
                        }
                    })
                }).catch(() => {
                    vm.$message.info('已取消操作')
                });
            }
        },


        // 多选
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },

        search: function(){
            if(!vm.dateRange) {  //如果日期选择器中先有选择，后置空，结果为null，会报错
                vm.dateRange = '';
            }
            vm.currentStartTime = vm.dateRange[0] || '';
            vm.currentEndTime = vm.dateRange[1] || '';

            vm.getUploadList(vm.currentPageSize, 1, vm.currentAdminName, vm.currentFileKey, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            vm.getUploadList(val, 1, vm.currentAdminName, vm.currentFileKey, vm.currentStartTime, vm.currentEndTime);
        },
        handleCurrentChange(val) {
            vm.getUploadList(vm.currentPageSize, val, vm.currentAdminName, vm.currentFileKey, vm.currentStartTime, vm.currentEndTime);
        }
    }
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');
