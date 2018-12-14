
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'user',
        pageTitle: '洗车用户',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        qrcodeDialogVisible: false,

        totalCnt: null,
        pageSizes: [1, 10, 20, 50],
        currentPageSize: null,
        currentPageIndex: null,

        dateRange: '',
        currentStartTime: '',
        currentEndTime: '',
        currentUserId: null,
        currentPhone: null,

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
        getUserList: function(pageSize, pageIndex, id, phone, startTime, endTime) {
            this.$http.post("/api/private/wash/user/list", {
                pageSize: pageSize,
                pageIndex: pageIndex,
                id: id,
                phone: phone,
                startTime: startTime,
                endTime: endTime
            }).then(function(res){
                let data = res.json().data;
                let result = data.result;
                let genderList = ['未知', '男', '女']
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.gender = genderList[val.gender]
                        val.createTime = formatTimestampToSecond(val.createTime)
                        val.lastLoginTime = formatTimestampToSecond(val.lastLoginTime)
                    })
                }
                vm.userList = result;
                vm.totalCnt = data.totalCnt;
                vm.currentPageSize = data.pageSize;
                vm.currentPageIndex = data.pageIndex;
            })
        },

        search: function(){
            if(!vm.dateRange) {  //如果日期选择器中先有选择，后置空，结果为null，会报错
                vm.dateRange = '';
            }
            vm.currentStartTime = vm.dateRange[0] || '';
            vm.currentEndTime = vm.dateRange[1] || '';
            vm.getUserList(vm.currentPageSize, 1, vm.currentUserId, vm.currentPhone, vm.currentStartTime, vm.currentEndTime);
        },

        handleSizeChange(val) {
            this.getUserList(val, 1, vm.currentUserId, vm.currentPhone, vm.currentStartTime, vm.currentEndTime)
        },
        handleCurrentChange(val) {
            this.getUserList(vm.currentPageSize, val, vm.currentUserId, vm.currentPhone, vm.currentStartTime, vm.currentEndTime)
        }
  
    }    
});

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

