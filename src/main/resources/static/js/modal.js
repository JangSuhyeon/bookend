function openDevPlanModal() {
    $("#devPlanModal").css("display", "block");
    $("#devPlanModal-background").css("display", "block");
}
$(function () {
    // 모달 닫기
    $(".close, #devPlanModal-background").click(function(){
        $("#devPlanModal").css("display", "none");
        $("#devPlanModal-background").css("display", "none");
    });

})