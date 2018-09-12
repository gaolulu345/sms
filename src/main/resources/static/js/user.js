
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'user',
        pageTitle: '用户列表',
        admin: admin,
        adminId: adminId,
        menuPer: menuPer,
        opPer: opPer,
        totalCnt: null,
        pageSizes: [1, 10, 20, 50],
        currentPageSize: null,
        currentPageIndex: null,
        // isAll: false,

        userList: [],
        userSelection: [],

        showEditUser: false,
        showAddUser: false,

        editUserInfo: {},
        addUserInfo: {
            // title: admin,
            // id: adminId
        },

        dialogLabelWidth: '200px',
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.getUserList(10, 1)
    },

    methods: {
        getUserList: function(pageSize, pageIndex) {
            this.$http.post("/api/private/admin/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                result.forEach(function(val) {
                    val.createTime = formatTimestampToSecond(val.createTime)
                    val.modifyTime = formatTimestampToSecond(val.modifyTime)
                    val.lastLoginTime = formatTimestampToSecond(val.lastLoginTime)
                })
                vm.userList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
        },

        // 注册用户
        addUser: function() {
            let infos = {
                name: vm.addUserInfo.name,
                intros: vm.addUserInfo.intros,
                username: vm.addUserInfo.username
            }
            this.$http.post("/api/private/admin/register", infos).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已添加');
                    vm.showAddUser = false;
                    window.location.reload();
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 修改用户信息
        clickEditUser(row) {
            console.log(row)
            vm.editUserInfo = row;
            vm.showEditUser = true;
        },
        editUser: function() {
            let data = {
                id: vm.editUserInfo.id,
                intros: vm.editUserInfo.intros,
                name: vm.editUserInfo.name
            }
            this.$http.post("/api/private/admin/update", data).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已修改');
                    vm.showEditUser = false;
                    vm.getUserList(vm.currentPageSize, vm.currentPageIndex);
                    // window.location.reload();
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 重置密码
        // resetPw: function(id) {
        //     vm.$confirm('确认重置' + id + '号用户登录密码?', '提示', {
        //         confirmButtonText: '确定',
        //         cancelButtonText: '取消',
        //         type: 'warning'
        //     }).then(() => {
        //         this.$http.post("/api/private/admin/reset/pw", {
        //             id: id
        //         }).then(function(res){
        //             let result = res.json();
        //             if(result.code == 200){
        //                 vm.$message.success('已重置');
        //                 vm.getUserList(vm.currentPageSize, vm.currentPageIndex);
        //             }else {
        //                 vm.$message.error(result.message)
        //             }
        //         })
        //     }).catch(() => {
        //         vm.$message.info('已取消操作')
        //     });
        // },
        // 批量删除用户
        handleUserSelectionChange(val) {
            this.userSelection = val;
        },
        deleteUser: function() {
            if (vm.userSelection.length == 0) {
                vm.$message.warning('请选择要删除的用户！');
            } else {
                let selection = vm.userSelection
                let ids = []
                for(var key in selection) {
                    ids.push(selection[key].id)
                }
                console.log(ids)

                vm.$confirm('确认删除这' + ids.length + '个用户?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.$http.post("/api/private/admin/update/delete", {
                        ids: ids,
                        deleted: true
                    }).then(function(res){
                        let result = res.json();
                        if(result.code == 200){
                            vm.$message.success('已删除');
                            vm.getUserList(vm.currentPageSize, vm.currentPageIndex);
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
            this.getUserList(val, 1)
        },
        handleCurrentChange(val) {
            this.getUserList(vm.currentPageSize, val)
        }
  
    }    
});




$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

