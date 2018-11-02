/**
 * @Author: jinghy
 * @Date: 2018/2/24/024 14:50
 */
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


    $scope.stages=  ['','幼苗期','生长期','收获期'];
    $scope.records=['','种植','护理','施肥','农药','环境','生长照片/视频','产品认证','企业资质'];

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