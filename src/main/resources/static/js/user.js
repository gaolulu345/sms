
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'user',
        pageTitle: '洗车用户',
        admin: admin,
        adminId: adminId,
        menuPer: menuPer,
        opPer: opPer,

        totalCnt: null,
        pageSizes: [1, 10, 20, 50],
        currentPageSize: null,
        currentPageIndex: null,

        dateRange: '',
        currentStartTime: '',
        currentEndTime: '',
        currentStatus: '',
        currentTerIds: [],

        userList: [],

        typeOptions: [{value: 0, label: '短信验证'}, {value: 1, label: '手机号+密码'}, {value: 2, label: 'APP内微信授权'}, {value: 3, label: '微信小程序'}, {value: 4, label: '支付宝小程序'}],
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();
            }
        },

        dialogLabelWidth: '200px',
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.getUserList(10, 1)
    },

    methods: {
        getUserList: function(pageSize, pageIndex) {
            this.$http.post("/api/private/user/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                result.forEach(function(val) {
                    val.createTime = formatTimestampToSecond(val.createTime)
                    val.lastLoginTime = formatTimestampToSecond(val.lastLoginTime)
                })
                vm.userList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
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

