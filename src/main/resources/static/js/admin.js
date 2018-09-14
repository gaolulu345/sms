
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'admin',
        pageTitle: '员工管理',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        totalCnt: null,
        pageSizes: [1, 10, 20, 50],
        currentPageSize: null,
        currentPageIndex: null,
        deleted: false,

        adminList: [],
        rolesList: [],
        adminSelection: [],

        showEditAdmin: false,
        showAddAdmin: false,

        editAdminInfo: {},
        addAdminInfo: {},

        dialogLabelWidth: '200px',
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.getAdminList(10, 1, false)
        this.getRolesList()
    },

    methods: {
        getAdminList: function(pageSize, pageIndex, deleted) {
            this.$http.post("/api/private/admin/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                deleted: deleted
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                result.forEach(function(val) {
                    val.createTime = formatTimestampToSecond(val.createTime)
                    val.modifyTime = formatTimestampToSecond(val.modifyTime)
                    val.lastLoginTime = formatTimestampToSecond(val.lastLoginTime)
                })
                vm.adminList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
                vm.deleted = data.deleted;
            })
        },

        getRolesList: function() {
            this.$http.post("/api/private/sys/list/roles", {
                pageSize: 10000,
                pageIndex: 1
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                vm.rolesList = result;
            })
        },

        // 注册
        addAdmin: function() {
            let infos = {
                name: vm.addAdminInfo.name,
                intros: vm.addAdminInfo.intros,
                username: vm.addAdminInfo.username,
                rolesId: vm.addAdminInfo.rolesId
            }
            this.$http.post("/api/private/admin/register", vm.addAdminInfo).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已添加');
                    vm.showAddAdmin = false;
                    window.location.reload();
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 修改信息
        clickEditAdmin(row) {
            console.log(row)
            vm.editAdminInfo = row;
            vm.showEditAdmin = true;
        },
        editAdmin: function() {
            let data = {
                id: vm.editAdminInfo.id,
                intros: vm.editAdminInfo.intros,
                name: vm.editAdminInfo.name,
                rolesId: vm.editAdminInfo.rolesId
            }
            this.$http.post("/api/private/admin/update", data).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已修改');
                    vm.showEditAdmin = false;
                    vm.getAdminList(vm.currentPageSize, vm.currentPageIndex, vm.deleted);
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 重置密码
        resetPw: function(id) {
            vm.$confirm('确认重置' + id + '号用户登录密码?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$http.post("/api/private/admin/reset/pw", {
                    id: id
                }).then(function(res){
                    let result = res.json();
                    if(result.code == 200){
                        vm.$message.success('已重置');
                        vm.getAdminList(vm.currentPageSize, vm.currentPageIndex, vm.deleted);
                    }else {
                        vm.$message.error(result.message)
                    }
                })
            }).catch(() => {
                vm.$message.info('已取消操作')
            });
        },
        // 批量删除
        handleAdminSelectionChange(val) {
            this.adminSelection = val;
        },
        updateDeleteAdmin: function(deleted) {
            let tip = deleted ? '删除' : '恢复'
            if (vm.adminSelection.length == 0) {
                vm.$message.warning('请选择要' + tip + '的员工！');
            } else {
                let selection = vm.adminSelection
                let ids = []
                for(var key in selection) {
                    ids.push(selection[key].id)
                }
                console.log(ids)

                vm.$confirm('确认' + tip + '这' + ids.length + '个员工?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.$http.post("/api/private/admin/update/delete", {
                        ids: ids,
                        deleted: deleted
                    }).then(function(res){
                        let result = res.json();
                        if(result.code == 200){
                            vm.$message.success('已' + tip);
                            vm.getAdminList(vm.currentPageSize, vm.currentPageIndex, vm.deleted);
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


        handleSizeChange(val) {
            this.getAdminList(val, 1, vm.deleted)
        },
        handleCurrentChange(val) {
            this.getAdminList(vm.currentPageSize, val, vm.deleted)
        }
  
    }    
});




$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

