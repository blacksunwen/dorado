/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
/*
* "transitions" plugin - Page change tranistions
*/

(function( $, window, undefined ) {

function css3TransitionHandler( name, reverse, $to, $from ) {

	var deferred = new $.Deferred(),
		reverseClass = reverse ? " reverse" : "",
		viewportClass = "ui-mobile-viewport-transitioning viewport-" + name,
		doneFunc = function() {

			$to.add( $from ).removeClass( "out in reverse " + name );

			if ( $from && $from[ 0 ] !== $to[ 0 ] ) {
				$from.removeClass( $.mobile.activePageClass );
			}

			$to.parent().removeClass( viewportClass );

			deferred.resolve( name, reverse, $to, $from );
		};

	$to.animationComplete( doneFunc );

	$to.parent().addClass( viewportClass );

	if ( $from ) {
		$from.addClass( name + " out" + reverseClass );
	}
	$to.addClass( $.mobile.activePageClass + " " + name + " in" + reverseClass );

	return deferred.promise();
}

// Make our transition handler public.
$.mobile.css3TransitionHandler = css3TransitionHandler;

// If the default transition handler is the 'none' handler, replace it with our handler.
if ( $.mobile.defaultTransitionHandler === $.mobile.noneTransitionHandler ) {
	$.mobile.defaultTransitionHandler = css3TransitionHandler;
}

})( jQuery, this );
