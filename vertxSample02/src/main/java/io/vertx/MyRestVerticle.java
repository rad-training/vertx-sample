package io.vertx;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.model.Whisky;

public class MyRestVerticle extends AbstractVerticle {

	private Map<Integer, Whisky> products = new HashMap<>();

	private void createProducts() {
		Whisky whisky1 = new Whisky("whiskey 1", "Scotland");
		Whisky whisky2 = new Whisky("whiskey 2", "Irland");
		products.put(whisky1.getId(), whisky1);
		products.put(whisky2.getId(), whisky2);
	}

	@Override
	public void start() {
		// on cree la list des produits
		createProducts();

		// create a router object to manage routes
		Router router = Router.router(vertx);
		// assigner le path (/) au handler qui affiche le message
		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/plain")
					.end("<html><body><h2>Hello From Vertx using Routers sur le uri:</h2></body></html>\n"
							+ routingContext.request().absoluteURI() + "\n" + Json.encodePrettily(products.values()));
		});
		// serve static resource from Assets Dir
		router.route("/assets/*").handler(StaticHandler.create("assets"));

		/**
		 * la list des methode pour le rest service on appel la methode get du
		 * router avec le path // this::getAll == routingcontext -> {
		 * routingcontext.response().putHeader("content-type",
		 * "application/json; charset=utf-8")
		 * .end(Json.encodePrettily(products.values())); });
		 */
		router.get("/api/whiskies").handler(this::getAll);

		/**
		 * router.route("/api/whiskies/*").handler(BodyHandler.create()); permet
		 * de pouvoir lire le contenu body de la requet envoyer au path
		 * api/whiskies
		 */
		router.route("/api/whiskies/*").handler(BodyHandler.create());
		router.post("/api/whiskies").handler(this::addOne);

		router.delete("/api/whiskies/:id").handler(this::deleteOne);

		// create le serveur qui va permettre de recevoirr les requets
		vertx.createHttpServer().requestHandler(router::accept).listen(1235);
	}

	@Override
	public void stop(Future<Void> fut) {

	}

	public void getAll(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(products.values()));
	}

	public void addOne(RoutingContext routingContext) {
		Whisky whisky3 = Json.decodeValue(routingContext.getBodyAsString(), Whisky.class);
		products.put(whisky3.getId(), whisky3);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(whisky3));
	}

	public void deleteOne(RoutingContext routingContext) {
		String idwhisky = routingContext.request().getParam("id");
		if (idwhisky == null) {
			routingContext.response().setStatusCode(404).end();
		} else {
			Integer id = Integer.valueOf(idwhisky);
			products.remove(id);
		}
		routingContext.response().setStatusCode(204).end();

	}
}
