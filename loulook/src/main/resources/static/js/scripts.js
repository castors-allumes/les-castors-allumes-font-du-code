$(window).load(function() {

	hackaton.init();

});

var hackaton = {

	init : function() {

		hackaton.hp();
		hackaton.callNewProducts();
			
	},

	hp : function() {

		var hp = $('.hp'),
			hp1 = $('.hp1'),
			hp2 = $('.hp2'),
			overlay = $('.hp-overlay'),
			tl = new TimelineLite();

			hp.on('click', function(e) {
				tl.to( hp1, 1.5, {
					x: '-100%', ease:Expo.easeInOutQuint
				}, 'hp')
				.to( hp2, 1.5, {
					x: '100%', ease:Expo.easeInOutQuint, onComplete: function() {
						hp.remove();
					}
				}, 'hp')
				.to( overlay, 1.5, {
					opacity: 0, ease:Expo.easeInOutQuint, onComplete: function() {
						overlay.remove();
					}
				}, 'hp');

			var random = Math.floor(Math.random() * 100) + 1;
				random = (random == 64) ? 65 : random;

				hackaton.ajaxCall( random );

			});

	},

	callNewProducts : function() {

		var select = $('.buttons select'),
			goBtn = $('.input .ok');

			select.on('change', function() {
				var idTheme = $(this).val();

					hackaton.ajaxCall( idTheme );
			});

			goBtn.on('click', function() {
				hackaton.sendBonus( $('.input input').val() );
			});
			

	},

	animOldProducts : function() {

		var tl = new TimelineLite(),
			origin = $('.products.origin'),
			originProducts = origin.find('.product');

			tl.to( origin, 2, {
				x: '-100%', ease:Expo.easeInOutQuint, onComplete: function() {
					origin.remove();
				}
			}, 'origin')
			.to( originProducts, 0.8, {
				rotation: -7, ease:Expo.easeInOutQuint, onComplete: function() {
					hackaton.animNewProducts();
				}
			}, 'origin');


	},

	animNewProducts : function() {

		var tl = new TimelineLite(),
			toload = $('.toload'),
			theme = $('.theme'),
			toloadProducts = toload.find('.product');

			tl.to( toload, 1.8, {
				x: 0, ease:Expo.easeInOutQuint
			}, 'hp')
			.to( theme, 1, {
				opacity: 1, ease:Expo.easeInOutQuint
			}, 'hp')
			.to( toloadProducts, 1, {
				rotation: 6, delay: 1, ease:Expo.easeInOutQuint
			}, 'hp')
			.to( toloadProducts, 0.5, {
				rotation: 0, delay: -0.1, ease:Expo.easeInOutQuint, onComplete: function() {
					$('.products').removeClass('toload').addClass('origin');
					$('.global').append('<div class="products toload"></div>');
					$('.products').removeAttr('style');
					$('.product').removeAttr('style');
				}
			});

	},

	ajaxCall : function(idTheme) {

		$.ajax({
			url: "/looks/"+idTheme,
			dataType: "json",
			success: function(data) {
			    var id = data.ID,
			    	theme = data.theme,
			    	accessoire = "images/product/ACCESSOIRES/" + data.images.accessoire,
			    	veste = "images/product/MANTEAUX/" + data.images.manteau,
			    	chemise = "images/product/HAUTS/" + data.images.haut,
			    	pantalon = "images/product/BAS/" + data.images.bas,
			    	chaussure = "images/product/CHAUSSURES/" + data.images.chaussures;

					if ( data.images.chaussures ) {
						$('.products.toload').prepend('<div class="product chaussure"><img src=""></div>');
			    		$('.toload .product.chaussure img').attr('src', chaussure);
					}
					if ( data.images.bas ) {
						$('.products.toload').prepend('<div class="product pantalon"><img src=""></div>');
			    		$('.toload .product.pantalon img').attr('src', pantalon);
					}
					if ( data.images.haut ) {
						$('.products.toload').prepend('<div class="product chemise"><img src=""></div>');
			    		$('.toload .product.chemise img').attr('src', chemise);
					}
					if ( data.images.manteau ) {
						$('.products.toload').prepend('<div class="product veste"><img src=""></div>');
			    		$('.toload .product.veste img').attr('src', veste);
					}
					if ( data.images.accessoire ) {
						$('.products.toload').prepend('<div class="product accessoire"><img src=""></div>');
			    		$('.toload .product.accessoire img').attr('src', accessoire);
					}

			    	$('.theme').text(theme);

			    	if ( $('.hp').length > 0 ) {
			    		hackaton.animNewProducts();
			    	} else {
			    		hackaton.animOldProducts();
			    	}
			}
		});

	},

	sendBonus : function(input) {

		var arr = input.split(' '),
			url = "/looks";

			$.ajax({
				url: url,
				type: 'POST',
    			data: { strings : arr },
				success: function(data){
				    var id = data.ID,
				    	theme = data.theme,
				    	accessoire = "images/product/ACCESSOIRES/" + data.images.accessoire,
				    	veste = "images/product/MANTEAUX/" + data.images.manteau,
				    	chemise = "images/product/HAUTS/" + data.images.haut,
				    	pantalon = "images/product/BAS/" + data.images.bas,
				    	chaussure = "images/product/CHAUSSURES/" + data.images.chaussures;

						if ( data.images.chaussures ) {
							$('.products.toload').prepend('<div class="product chaussure"><img src=""></div>');
				    		$('.toload .product.chaussure img').attr('src', chaussure);
						}
						if ( data.images.bas ) {
							$('.products.toload').prepend('<div class="product pantalon"><img src=""></div>');
				    		$('.toload .product.pantalon img').attr('src', pantalon);
						}
						if ( data.images.haut ) {
							$('.products.toload').prepend('<div class="product chemise"><img src=""></div>');
				    		$('.toload .product.chemise img').attr('src', chemise);
						}
						if ( data.images.manteau ) {
							$('.products.toload').prepend('<div class="product veste"><img src=""></div>');
				    		$('.toload .product.veste img').attr('src', veste);
						}
						if ( data.images.accessoire ) {
							$('.products.toload').prepend('<div class="product accessoire"><img src=""></div>');
				    		$('.toload .product.accessoire img').attr('src', accessoire);
						}

				    	$('.theme').text(theme);

				    	hackaton.animOldProducts();
				}
			});

	}

};