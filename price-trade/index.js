/**
 * Responds to any HTTP request that can provide a "message" field in the body.
 *
 * @param {!Object} req Cloud Function request context.
 * @param {!Object} res Cloud Function response context.'
 * Test: Example input: {"message": "Hello!"}
 * docs: 
 *	https://cloud.google.com/pubsub/
 *	https://cloud.google.com/functions/docs/quickstart
 *	https://cloud.google.com/functions/docs/tutorials/http
 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"trade":{"tradeId":"102333","tradeType":"FX Spot"}}' https://us-central1-stelinno-paid.cloudfunctions.net/price-trade
 */
exports.priceTrade = function priceTrade(req, res) {
  var trade = req.body.trade;
  if (trade === undefined) {
    // This is an error case, as "trade" is required.
    res.status(400).send('No trade found in input!');
  } else {
    // Everything is okay.
    console.log(trade);
    res.status(200).send('Trade with id [' + trade.tradeId + '] and a trade type of [' + trade.tradeType + '] was priced successfully');
  }
};
