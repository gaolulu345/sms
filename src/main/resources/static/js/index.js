
var vm = new Vue({
    el: "#app",
    data: {
    	pageName: 'index',
        pageTitle: '首页',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,

        
       


        // menuPer: menuPer,
        // opPer: opPer
    },
    mounted: function() {
        
    } 
});


$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');


