/**
 * @Author: jinghy
 * @Date: 2018/2/24/024 14:50
 */
app.controller('home', function ($http, $scope, $rootScope, $state) {
    $scope.myKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.searchRdcs();
        }
    };

    $scope.searchRdcs=function(){
        var uuid=$("#text_search").val();
        if(uuid.length==14){
            $http({url:"/vserUUID.do",method:"post", params: {"token":$rootScope.getToken(),"uuid":uuid}}).success(function (data) {
                if(data){
                    $state.go('tabs.gooddeatil',{id:null,uuid:uuid});
                }else{
                    alert("溯源码不正确！请注意商品安全！");
                }
            });
        }else{
            alert("请输入正确的编码");
        }


    };
    $scope.initData=function(){
        $http({url:"/goodlist.do",method:"post", params: {"token":$rootScope.getToken(),"goodtype":1,"rows":5}}).success(function (data) {
                $scope.hm_sg=data.rows;
        });
        $http({url:"/goodlist.do",method:"post", params: {"token":$rootScope.getToken(),"goodtype":2,"rows":5}}).success(function (data) {
                $scope.hm_sc=data.rows;
        });
    }
    $scope.gogoodList=function(t){
        $state.go('tabs.goodlist',{goodtype:t});
    }
    $scope.gogoodDeatil=function(t){
        $state.go('tabs.gooddeatil',{id:t});
    }

    $scope.initData();

});