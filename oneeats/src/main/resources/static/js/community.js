$(document).ready(function () {
		var overlayDiv = $('<div class="overlay_gray"></div>');
  		$('body').append(overlayDiv);
        $(".hoverexpand").hover(
          function () {
            $(this).addClass("hovered");
            $('.overlay_gray').show();
          },
          function () {
            $(this).removeClass("hovered");
            $('.overlay_gray').hide();
          }
        );
        
        
        $('#plus_btn').on('click',function(){
			console.log("plus ingredient");
			var newRow= `
			<div class="row ingredient_row">
                    <div class="col-md">
                      <input type="text" name="option_name" class="form-control" placeholder="이름" />
                    </div>
                    <div class="col-md">
                      <input type="text" name="option_qty" class="form-control" placeholder="양" />
                    </div>
                     <div class="col-md">
                      <input type="text" name="option_price" class="form-control" placeholder="가격" />
                    </div>
                    <div class="col-md-1">
                      <img
                        id="minus_btn"
                        class="btn-smallsquare border"
                        src="/img/icon/minus.png"
                        alt="빼기 버튼"
                      />
                    </div>
                  </div>
			`;
			$('.ingredient_col').append(newRow);
			
		});
		
      });
      
      
$(document).on('click','#minus_btn',function(){
	console.log("마이너스 버튼 클릭");
	$(this).parent().parent().remove();
	
});
      