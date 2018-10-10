var menuPermission, opPermission, menuPermission1, opPermission1
var menu
var vm = new Vue({
    el: "#app",
    data: {
    	name: '',
        pw: '',
        cip: returnCitySN.cip || '',

    },
    methods: {
    	login: function(){
    		var data = {username: vm.name, password: hex_md5(vm.pw), cip: vm.cip};
			$.ajax({
				url: 'api/user/login',
				dataType: 'json',
				type: 'post',
				async: false,
				data: JSON.stringify(data),
				contentType : 'application/json',
				success: function(data){
					console.log(data);
					if(data.code == 200){
						vm.$message.success('登录成功');
						sessionStorage.setItem("adminId", data.data.id); 
						sessionStorage.setItem("adminRoleId", data.data.rolesId); 
						sessionStorage.setItem("admin", data.data.name); 
						window.location.href = '/pages/index';
					} else {
						vm.$message.error(data.message);
					}
				}
			})
    	}
    }
    
});

function keyLogin(event){
	if (event.keyCode == 13){
		vm.login();
	}
}