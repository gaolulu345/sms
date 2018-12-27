
var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'index',
        pageTitle: '登录日志',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        qrcodeDialogVisible: false,
        more: false,
        logList: [],
    },
    mounted: function() {
        this.getLogList(false)
    },
    methods: {
        getLogList: function(more){
            this.$http.post("/api/private/admin/login/log", {
                more: more
            }).then(function(res){
                let data = res.json().data
                console.log(data)
                let result = data.result;
                if(result && result[0]) {
                    result.forEach(function(val) {
                        val.createTime = val.createTime ? formatTimestampToSecond(val.createTime) : '暂无'
                    })
                }
                vm.logList = result
            })
        },
        showMore: function() {
            vm.more = true
            this.getLogList(true)
        }
    }
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');


