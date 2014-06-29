var expect = require("expect-webelements");
var _      = require("lodash");

describe("A user", function() {
  
  it("should find Minium Github page when he searches for it", function() {
    get(wd, config.googleUrl);
    
    var linkUrl = "https://github.com/viltgroup/minium";

    // elements
    var searchbox = $(wd, ":input").withName("q");
    var button = $(wd, "button").withAttr("aria-label", "Google Search");
    var links = $(wd, "a");
    var link = links.withAttr("data-href", linkUrl).add(links.withAttr("href", linkUrl));
    
    // actions
    fill(searchbox, "Minium Github");
    click(button);
    
    // verifications
    expect(link).to.have.size(1);
  });
  
  it("should do several searches and find proper results", function() {
    get(wd, config.googleUrl);
    
    for (var query in config.searches) {
      var linkUrls = config.searches[query];
      
      // elements
      var searchbox = $(wd, ":input").withName("q");
      var button = $(wd, "button").withAttr("aria-label", "Google Search");
      var links = $(wd, "a");
      
      fill(searchbox, query);
      click(button);
      
      expect(linkUrls).not.to.be.empty();
      
      _(linkUrls).forEach(function (linkUrl) {
        var link = links.withAttr("data-href", linkUrl).add(links.withAttr("href", linkUrl));
        expect(link).to.have.size(1);
      });
    }
  });

});