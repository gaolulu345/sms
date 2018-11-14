
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'merchant',
        pageTitle: '商家中心登陆账号管理',
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

        merchantList: [],
        partnerList: [],
        partnerName: {},

        showConnectPartner: false,
        connectInfo: {},

        dialogLabelWidth: '200px',
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.getMerchantList(10, 1)
        this.getPartnerList(100000, 1)
    },

    methods: {
        getMerchantList: function(pageSize, pageIndex) {
            this.$http.post("/api/private/merchant/employee/list", {
                pageSize: pageSize,
                pageIndex: pageIndex
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                result.forEach(function(val) {
                    val.lastLoginTime = formatTimestampToSecond(val.lastLoginTime)
                })
                vm.merchantList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
        },

        getPartnerList: function(pageSize, pageIndex) {
            this.$http.post("/api/private/merchant/employee/selection/partner", {
                pageSize: pageSize,
                pageIndex: pageIndex
            }).then(function(res){
                let data = res.json().data
                let result = data.result
                vm.partnerList = result
                let partnerName = {}
                result.forEach(function(val) {
                    partnerName[val.id] = val.title + '(' + val.intros + ')'
                })
                vm.partnerName = partnerName
            })
        },

        // 删除、禁用
        clickDelete: function(id, deleted) {
            let tip = deleted ? '禁用' : '解禁'
            vm.$confirm('确认' + tip + '第' + id + '号商家账号?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                vm.$http.post("/api/private/merchant/employee/update/delete", {
                    ids: [id],
                    deleted: deleted
                }).then(function(res){
                    let result = res.json();
                    if(result.code == 200){
                        vm.$message.success('已' + tip);
                        vm.getMerchantList(vm.currentPageSize, vm.currentPageIndex);
                    }else {
                        vm.$message.error(result.message)
                    }
                })
            }).catch(() => {
                vm.$message.info('已取消操作')
            });
        },

        clickConnect: function(id, name) {
            vm.connectInfo.id = id
            vm.connectInfo.name = name
            vm.showConnectPartner = true
        },

        // 关联并激活
        clickEnable: function() {
            console.log(vm.connectInfo)
            let id = vm.connectInfo.id
            let name = vm.connectInfo.name
            let partnerId = vm.connectInfo.partnerId
 
            vm.$http.post("/api/private/merchant/employee/update/enable", {
                id: id,
                partnerId: partnerId,
                enable: true
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已关联并激活');
                    vm.showConnectPartner = false;
                    vm.connectInfo = {}
                    vm.getMerchantList(vm.currentPageSize, vm.currentPageIndex);
                }else {
                    vm.$message.error(result.message)
                }
            })
        },

        handleSizeChange(val) {
            this.getMerchantList(val, 1)
        },
        handleCurrentChange(val) {
            this.getMerchantList(vm.currentPageSize, val)
        }
  
    }    
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

