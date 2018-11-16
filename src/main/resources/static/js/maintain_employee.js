
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'maintain',
        pageTitle: '维保管理',
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

        maintainList: [],

        // dialogLabelWidth: '200px',
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.getMaintainList(10, 1)
    },

    methods: {
        getMaintainList: function(pageSize, pageIndex) {
            this.$http.post("/api/private/maintion/employee/list", {
                pageSize: pageSize,
                pageIndex: pageIndex
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                result.forEach(function(val) {
                    val.lastLoginTime = formatTimestampToSecond(val.lastLoginTime)
                })
                vm.maintainList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
        },

        // 删除、禁用
        clickDelete: function(id, deleted) {
            let tip = deleted ? '禁用' : '解禁'
            vm.$confirm('确认' + tip + '第' + id + '号维保人员?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                vm.$http.post("/api/private/maintion/employee/update/delete", {
                    ids: [id],
                    deleted: deleted
                }).then(function(res){
                    let result = res.json();
                    if(result.code == 200){
                        vm.$message.success('已' + tip);
                        vm.getMaintainList(vm.currentPageSize, vm.currentPageIndex);
                    }else {
                        vm.$message.error(result.message)
                    }
                })
            }).catch(() => {
                vm.$message.info('已取消操作')
            });
        },

        // 审核
        clickEnable: function(id, enable) {
            let tip = enable ? '通过' : '拒绝'
            vm.$confirm('确认' + tip + '第' + id + '号维保人员?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                vm.$http.post("/api/private/maintion/employee/update/enable", {
                    id: id,
                    enable: enable
                }).then(function(res){
                    let result = res.json();
                    if(result.code == 200){
                        vm.$message.success('已' + tip);
                        vm.getMaintainList(vm.currentPageSize, vm.currentPageIndex);
                    }else {
                        vm.$message.error(result.message)
                    }
                })
            }).catch(() => {
                vm.$message.info('已取消操作')
            });
        },

        handleSizeChange(val) {
            this.getMaintainList(val, 1)
        },
        handleCurrentChange(val) {
            this.getMaintainList(vm.currentPageSize, val)
        }
  
    }    
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

