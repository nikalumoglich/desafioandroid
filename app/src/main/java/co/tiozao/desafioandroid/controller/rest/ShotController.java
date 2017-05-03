package co.tiozao.desafioandroid.controller.rest;

import co.tiozao.desafioandroid.controller.rest.service.ShotsService;

public class ShotController {

    private ShotsService service;

    private String nextPageURL;

    public ShotController() {
        if (RetrofitController.instance == null) {
            RetrofitController.initialize();
        }

        service = RetrofitController.instance.createService(ShotsService.class);
    }

    public ShotsService getService() {
        return service;
    }

    public void setService(ShotsService service) {
        this.service = service;
    }

    public String getNextPageURL() {
        return nextPageURL;
    }

    public void setNextPageURL(String nextPageURL) {
        this.nextPageURL = nextPageURL;
    }
}
