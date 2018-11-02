var app=angular.module('ionicApp', ['ionic', "oc.lazyLoad"]);
app.config(["$provide", "$compileProvider", "$controllerProvider", "$filterProvider", function ($provide, $compileProvider, $controllerProvider, $filterProvider) {
    app.controller = $controllerProvider.register;
    app.directive = $compileProvider.directive;
    app.filter = $filterProvider.register;
    app.factory = $provide.factory;
    app.service = $provide.service;
    app.constant = $provide.constant;
}]);
app.run(['$rootScope', '$http', '$timeout', '$interval', '$ionicHistory', '$ionicTabsDelegate', '$ionicScrollDelegate', '$ionicLoading', '$ionicPopup', '$state', '$ionicPlatform',
    function ($rootScope, $http, $timeout, $interval, $ionicHistory, $ionicTabsDelegate, $ionicScrollDelegate, $ionicLoading, $ionicPopup, $state, $ionicPlatform) {
        $http.defaults.withCredentials = true;
        $http.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'};
        $rootScope.imgsv="//123.206.197.208:8989/";///
        $rootScope.gd_leve=['','精品','特优','一级','二级','精选' ];
        $rootScope.gd_unit=['','g','500g','kg','袋','箱'];
        $rootScope.getToken=function(){ return new Date().getHours()%4+3};

        /*全局alert更改*/
        window.alert = function (message) {
            var alertPopup = $ionicPopup.alert({
                title: '温馨提示',
                template: message
            });
        };
        $rootScope.onSwipeRight = function () {//向右滑动
            $ionicHistory.goBack();
        };
        $rootScope.onDragRight = function () {//向右拖拽
            $ionicHistory.goBack();
        }

    }]);
app .config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('tabs', {
                url: "/tab",
                abstract: true,
                templateUrl: "templates/tabs.html"
            })
            .state('tabs.home', {
                url: "/home",
                views: {
                    'home-tab': {
                        templateUrl: "app/com/templates/home.html?v=3.7.4",
                        controller: 'home'
                        // , resolve: {
                        //                 deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                        //                     return $ocLazyLoad.load("app/com/js/home.js" + version);
                        //                 }]
                        //             }
                    }
                }
            })
            .state('tabs.goodlist', {
                url: "/goodlist",
                cache: false,
                params: {"goodtype": null},
                views: {
                    'goodlist-tab': {
                        templateUrl: "app/com/templates/goodlist.html?v=3.7.4",
                        controller: 'goodlist'
                    }
                }
            })
            .state('tabs.gooddeatil', {
                url: "/gooddeatil/:id/:uuid",
                views: {
                    'goodlist-tab': {
                        templateUrl: "app/com/templates/gooddeatil.html?v=3.7.4",
                        controller: 'gooddeatil'
                    }
                }
            })
            .state('tabs.user', {
                url: "/user",
                views: {
                    'user-tab': {
                        templateUrl: "app/com/templates/user.html?v=3.7.4",
                        controller: 'user'
                    }
                }
            });


         $urlRouterProvider.otherwise("/tab/home");

    }) ;

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
        if(uuid.length==16){
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
/**
 * @Author: jinghy
 * @Date: 2018/2/24/024 14:50
 */
app.controller("goodlist",function($scope,$rootScope, $http, $timeout, $ionicScrollDelegate,  $stateParams, $ionicLoading,$ionicSlideBoxDelegate){
    $scope.paginfo={pageNum:1,pageSize:10 ,coleam:"name"  };
    $scope.goodlist=[];

    $scope.myKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.paginfo.colval= $("#goodlist_search").val();
            $scope.initData();
        }
    };

    $scope.initData=function(isadd){

        $ionicLoading.show();
        $scope.paginfo.token=$rootScope.getToken();
        $http({url:"/goodlist.do",method:"post", params: $scope.paginfo}).success(function (data) {
            $scope.paginfo.pageNum= data.pageNum;
            $scope.paginfo.total=data.total;//总记录
            $scope.goodlist=data.rows;
            //  $scope.initTemp(data.rows,isadd );
            $ionicLoading.hide();
        });
    }
    $scope.initTemp=function(data,isadd){
        var dttalis=[];
        angular.forEach(data, function (item) {
            dttalis.push(
                ['<ion-item class="item item-thumbnail-left" href="#/gooddeatil/',item.id,'"><img src="',$rootScope.imgsv+item.img,'" class="imgLost"> <h2>',item.name,'<span class="fr sm">',$rootScope.gd_leve[item.grade]    +"   "+ item.price+'元/'+$rootScope.gd_unit[item.unit],'</span></h2>' ,
                    ' <p class="color-99" style="font-size: 13px;"><i class="iconfont icon-techreport-"></i>',item.compname,'</p>' ,
                    '<p style="font-size: 13px;">',item.addtime,'</p>' ,
                    '</ion-item>'].join("")
            );
        });
        if(isadd){
            $("#ionc_goodlist").append(dttalis.join(""));
        }else{
            $("#ionc_goodlist").html(dttalis.join(""));
        }
    }



    $scope.can_loadMore=function(flag){
        if(!$scope.paginfo.total){ return true;}
        return  $scope.goodlist.length<$scope.paginfo.total ;
    }
    $scope.loadMore=function(){
        $scope.paginfo.pageNum++;
        $scope.initData();
        $scope.$broadcast("scroll.infiniteScrollComplete");
    }
    $scope.initData();
});

app.controller("gooddeatil",function($scope,$rootScope,$state, $http, $timeout, $ionicScrollDelegate,  $stateParams, $ionicLoading,$ionicSlideBoxDelegate ){
    $scope.goback=function(){   $state.go('tabs.goodlist'); };
    $scope.show_trace=null;
    $scope.show_imgs=null;
    $scope.showrs=function(olddata){
        $scope.show_trace=olddata;
        $http({url:"/tracesImgs/"+olddata.id+".do",method:"post"}).success(function (data) {
            // data.push({ id:0,location:olddata.img });
            $scope.show_imgs=data;
            //$ionicSlideBoxDelegate.update();
            $ionicSlideBoxDelegate.$getByHandle('image-viewer').update();
            // $("#trace_info").show();
            $timeout(function () {
                layer.open({ type: 1 ,content: $("#trace_info").html()  ,anim: 'up'  ,style: 'position:fixed; bottom:0; left:0; width: 100%; height: auto; padding:10px 0; border:none;'});
            }, 200);
        });
    };


    $scope.stages=  ['幼苗期','生长期','收获期'];
    $scope.records=['种植','护理','施肥','农药','环境','生长照片/视频','产品认证','企业资质'];

    $scope.initData=function(isadd){
        $ionicLoading.show();
        $http({url:"/gooddeatil.do",method:"post", params: {token:$rootScope.getToken(),id:$stateParams.id,uuid:$stateParams.uuid  }}).success(function (data) {
            $ionicLoading.hide();
            if(200==data.status){
                $scope.company=data.entity.company;//公司信息
                $scope.copimgs=data.entity.copimgs;//公司图片
                $scope.grow=data.entity.grow;//基地信息
                $scope.growimgs=data.entity.growimgs;//基地图片
                $scope.imgs=data.entity.imgs;//商品图片信息
                $scope.goodinfo=data.entity.goodinfo;//商品信息
                $scope.traces=data.entity.traces; //追溯记录
                $scope.goodAuth=data.entity.goodAuth; //追溯记录
                $ionicSlideBoxDelegate.update();
            }else{
                alert(data.message);
                $timeout(function () {
                    $state.go('tabs.home');
                }, 1500);

            }
        });
    }
    if($stateParams.id==undefined&&$stateParams.uuid==undefined){
        $state.go('tabs.home');
    }else{
        $scope.initData();
    }

});
/**
 * @Author: jinghy
 * @Date: 2018/2/24/024 14:50
 */
app.controller('user', function ($http, $scope, $rootScope, $state, $ionicPopup, $timeout, $ionicLoading, $ionicSlideBoxDelegate) {

});