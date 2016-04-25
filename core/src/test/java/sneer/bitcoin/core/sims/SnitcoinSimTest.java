package sneer.bitcoin.core.sims;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import sneer.bitcoin.core.ExchangeRate;

import static org.junit.Assert.assertEquals;
import static sneer.bitcoin.core.util.Constants.PARAMS;
import static sneer.bitcoin.core.util.Constants.TO;

public class SnitcoinSimTest {

	private static final double DELTA = 0;
	private sneer.bitcoin.core.sims.SnitcoinSim snitcoin;
	private ExchangeRate usd;
	private ExchangeRate brl;
	private Address address;

	@Before
	public void setUp() throws Exception {
		snitcoin = new sneer.bitcoin.core.sims.SnitcoinSim();
		usd = snitcoin.exchangeRates.get(0);
		brl = snitcoin.exchangeRates.get(2);
		snitcoin.setDefault(usd);
		address = new Address(PARAMS, TO);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCurrentReceiveAddress() throws AddressFormatException {
		assertEquals(snitcoin.currentReceiveAddress(), address);
	}

	@Test
	public void testBalanceInBTC() throws Exception {
		assertEquals(snitcoin.balanceInBTC(), BigDecimal.valueOf(0.042654));
	}

	@Test
	public void testBalanceConverted() throws Exception {
		assertEquals(snitcoin.balanceConverted(), 18.64, DELTA);
	}

	@Test
	public void testCurrentDefaultRate() throws Exception {
		assertEquals(snitcoin.currentDefaultRate(), usd);
	}

	@Test
	public void testSetDefault() throws Exception {
		snitcoin.setDefault(brl);
		assertEquals(snitcoin.currentDefaultRate(), brl);

		snitcoin.setDefault(usd);
		assertEquals(snitcoin.currentDefaultRate(), usd);
	}

	@Test
	public void testRateBy() throws Exception {
		assertEquals(snitcoin.rateBy("USD"), usd);
		assertEquals(snitcoin.rateBy("BRL"), brl);
	}

	@Test
	public void testAmountInBTC() throws Exception {
		assertEquals(snitcoin.amountInBTC(42.00), BigDecimal.valueOf(.096114));
		assertEquals(snitcoin.amountInBTC(531.00), BigDecimal.valueOf(1.215159));
	}

	@Test
	public void testAmountConverted() throws Exception {
		assertEquals(snitcoin.amountConverted(BigDecimal.valueOf(1)), usd.rate, DELTA);
	}

}
