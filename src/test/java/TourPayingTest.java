import data.DataGenerator;
import data.SQLHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CreditPage;
import pages.MainPage;
import pages.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;


public class TourPayingTest {

    PaymentPage paymentPage;
    CreditPage creditPage;

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("Should open payment page")
    void shouldOpenPaymentPage() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
    }

    @Test
    @DisplayName("Should open credit page")
    void shouldOpenCreditPage() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
    }

    @Test
    @DisplayName("Card payment should be successful with APPROVED card")
    void shouldSuccessfullyPayIfCardApproved() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var successfulPayment = paymentPage.successfulPayment(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode());
        var getPaymentStatus = SQLHelper.getPaymentStatus();
        var getPaymentId = SQLHelper.getPaymentIdFromDB();
        var getOrderInfo = SQLHelper.getPaymentInfoFromDB();
        assertAll("Получить статус оплаты 'APPROVED' и наличие заказа в БД",
                () -> assertEquals("APPROVED", getPaymentStatus),
                () -> assertEquals(getPaymentId, getOrderInfo)
        );
    }

    @Test
    @DisplayName("Credit payment should be successful with APPROVED card")
    void shouldSuccessfullyCreditPayIfCardApproved() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var successfulCreditPayment = creditPage.successfulCreditPayment(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode());
        var getCreditStatus = SQLHelper.getCreditStatus();
        var getCreditId = SQLHelper.getCreditRequestIdFromDB();
        var getOrderInfo = SQLHelper.getCreditRequestInfoFromDB();
        assertAll("Получить статус оплаты 'APPROVED' по кредитной карте и наличие заказа в БД",
                () -> assertEquals("APPROVED", getCreditStatus),
                () -> assertEquals(getCreditId, getOrderInfo)
        );
    }

    @Test
    @DisplayName("Card payment should be failed with DECLINED card")
    void shouldNotPayIfDeclinedCard() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var declinedCard = DataGenerator.getDeclinedCardInfo();
        var invalidCardPayment = paymentPage.failedPayment(String.valueOf(DataGenerator.getDeclinedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode());
        var getPaymentStatus = SQLHelper.getPaymentStatus();
        var getPaymentInfo = SQLHelper.getPaymentIdFromDB();
        var getOrderInfo = SQLHelper.getPaymentInfoFromDB();
        assertAll("Получить статус оплаты 'DECLINED' и отсутствие заказа в БД",
                () -> assertEquals("DECLINED", getPaymentStatus),
                () -> assertNotEquals(getPaymentInfo, getOrderInfo)
        );
    }

    @Test
    @DisplayName("Credit payment should be failed with DECLINED card")
    void shouldNotPayCreditIfDeclinedCard() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var declinedCard = DataGenerator.getDeclinedCardInfo();
        var failedCreditPayment = creditPage.failedCreditPayment(String.valueOf(DataGenerator.getDeclinedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode());
        var getCreditStatus = SQLHelper.getCreditStatus();
        var getCreditId = SQLHelper.getCreditRequestIdFromDB();
        var getOrderInfo = SQLHelper.getCreditRequestInfoFromDB();
        assertAll("Получить статус оплаты 'DECLINED' по кредитной карте и отсутствие заказа в БД",
                () -> assertEquals("DECLINED", getCreditStatus),
                () -> assertNotEquals(getCreditId, getOrderInfo)
        );
    }

    @Test
    @DisplayName("Should be error if card number field is empty when card pay")
    void shouldBeErrorIfCardNumberEmptyWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var paymentNoCard = paymentPage.paymentCardNumberEmptyField(DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if card number field is empty when credit pay")
    void shouldBeErrorIfCardNumberEmptyWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var creditPaymentNoCard = creditPage.creditPaymentCardNumberEmptyField(DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if month field is empty when card pay")
    void shouldBeErrorIfMonthEmptyWhenPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentNoMonth = paymentPage.paymentMonthEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if month field is empty when credit pay")
    void shouldBeErrorIfMonthEmptyWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var creditPaymentNoCard = creditPage.creditPaymentMonthEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if year field is empty when card pay")
    void shouldBeErrorIfYearEmptyWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentNoYear = paymentPage.paymentYearEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if year field is empty when credit pay")
    void shouldBeErrorIfYearEmptyWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentNoYear = creditPage.creditPaymentYearEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if card owner field is empty when card pay")
    void shouldBeErrorIfCardOwnerEmptyWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentNoOwner = paymentPage.paymentOwnerEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if card owner field is empty when credit pay")
    void shouldBeErrorIfCardOwnerEmptyWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentNoOwner = creditPage.creditPaymentOwnerEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidCVCCode(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if CVC/CVV field is empty when card pay")
    void shouldBeErrorIfCVCEmptyWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentNoCVC = paymentPage.paymentCVCEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if CVC/CVV field is empty when credit pay")
    void shouldBeErrorIfCVCEmptyWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentNoCVC = creditPage.creditPaymentCVCEmptyField(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should be error if card number is short when card pay")
    void shouldBeErrorIfShortCardNumberWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var paymentShortCardNumber = paymentPage.paymentCardNumberShort(String.valueOf(DataGenerator.generateInvalidCardNumberShort()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if card number is short when credit")
    void shouldBeErrorIfShortCardNumberWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var creditPaymentShortCardNumber = creditPage.creditPaymentCardNumberShort(String.valueOf(DataGenerator.generateInvalidCardNumberShort()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if card number is not in DB when card pay")
    void shouldBeErrorIfCardNumberIsNotInDBWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var paymentCardNotInDB = paymentPage.failedPayment(DataGenerator.generateCardNumberNotInDB(),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode());
    }

    @Test
    @DisplayName("Should be error if card number is not in DB when credit pay")
    void shouldBeErrorIfCardNumberIsNotInDBWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var declinedCard = DataGenerator.getDeclinedCardInfo();
        var failedCreditPayment = creditPage.failedCreditPayment(String.valueOf(DataGenerator.generateCardNumberNotInDB()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode());
    }

    @Test
    @DisplayName("Should be error if 1 digit in month field when card pay")
    void shouldBeErrorIf1NumberInMonthWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var payment1DigitMonth = paymentPage.paymentMonthWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generate1DigitNumber(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 digit in month field when credit pay")
    void shouldBeErrorIf1NumberInMonthWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPayment1DigitMonth = creditPage.creditPaymentMonthWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generate1DigitNumber(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if card expired when card pay")
    void shouldBeErrorIfExpiredCardWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var expiredCard = DataGenerator.getCardExpired();
        paymentPage.payment(DataGenerator.getCardExpired().getNumber(),
                DataGenerator.getCardExpired().getMonth(),
                DataGenerator.getCardExpired().getYear(),
                DataGenerator.getCardExpired().getCardOwner(),
                DataGenerator.getCardExpired().getCVC());
        paymentPage.yearError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Should be error if card expired when credit pay")
    void shouldBeErrorIfExpiredCardWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var expiredCard = DataGenerator.getCardExpired();
        creditPage.creditPayment(DataGenerator.getCardExpired().getNumber(),
                DataGenerator.getCardExpired().getMonth(),
                DataGenerator.getCardExpired().getYear(),
                DataGenerator.getCardExpired().getCardOwner(),
                DataGenerator.getCardExpired().getCVC());
        creditPage.yearError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Should be error if card is future when card pay")
    void shouldBeErrorIfFutureCardWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var expiredCard = DataGenerator.getCardFuture();
        paymentPage.payment(DataGenerator.getCardFuture().getNumber(),
                DataGenerator.getCardFuture().getMonth(),
                DataGenerator.getCardFuture().getYear(),
                DataGenerator.getCardFuture().getCardOwner(),
                DataGenerator.getCardFuture().getCVC());
        paymentPage.yearError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Should be error if card is future when credit pay")
    void shouldBeErrorIfFutureCardWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var expiredCard = DataGenerator.getCardFuture();
        creditPage.creditPayment(DataGenerator.getCardFuture().getNumber(),
                DataGenerator.getCardFuture().getMonth(),
                DataGenerator.getCardFuture().getYear(),
                DataGenerator.getCardFuture().getCardOwner(),
                DataGenerator.getCardFuture().getCVC());
        creditPage.yearError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Should be error if '00' in month field when card pay")
    void shouldBeErrorIfZerosInMonthWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentZerosMonth = paymentPage.paymentMonthWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                "00",
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if '00' in month field when credit pay")
    void shouldBeErrorIfZerosInMonthWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentZerosMonth = creditPage.creditPaymentMonthWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                "00",
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 digit in year field when card pay")
    void shouldBeErrorIf1DigitInYearWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentWrongYearFormat = paymentPage.paymentYearWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generate1DigitNumber()),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 digit in year field when credit pay")
    void shouldBeErrorIf1DigitInYearWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentWrongYearFormat = creditPage.creditPaymentYearWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generate1DigitNumber()),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 word in card owner field when card pay")
    void shouldBeErrorIf1WordInCardOwnerWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentOneWordOwner = paymentPage.paymentOwnerWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateOneWordInvalidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 word in card owner field when credit pay")
    void shouldBeErrorIf1WordInCardOwnerWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentOneWordOwner = creditPage.creditPaymentOwnerWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateOneWordInvalidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if cyrillic in card owner field when card pay")
    void shouldBeErrorIfCyrillicInCardOwnerWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentCyrillicOwner = paymentPage.paymentOwnerWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateCyrillicInvalidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if cyrillic in card owner field when credit pay")
    void shouldBeErrorIfCyrillicInCardOwnerWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentCyrillicOwner = creditPage.creditPaymentOwnerWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateCyrillicInvalidOwnerName(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if numbers in card owner field when card pay")
    void shouldBeErrorIfNumbersInCardOwnerWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentNumbersOwner = paymentPage.paymentOwnerWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateInvalidOwnerNameNumbers(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if numbers in card owner field when credit pay")
    void shouldBeErrorIfNumbersInCardOwnerWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentNumbersOwner = creditPage.creditPaymentOwnerWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateInvalidOwnerNameNumbers(),
                DataGenerator.generateValidCVCCode(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 digit in CVC/CVV field when card pay")
    void shouldBeErrorIf1DigitInCVCWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var payment1DigitCVC = paymentPage.paymentCVCWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generate1DigitNumber(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 1 digit in CVC/CVV field when credit pay")
    void shouldBeErrorIf1DigitInCVCWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPayment1DigitCVC = creditPage.creditPaymentCVCWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generate1DigitNumber(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 2 digits in CVC/CVV field when card pay")
    void shouldBeErrorIf2DigitsInCVCWhenPayment() {
        var mainPage = new MainPage();
        paymentPage = mainPage.tourPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var paymentCyrillicOwner = paymentPage.paymentCVCWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generate2DigitsNumber(),
                "Неверный формат");
    }

    @Test
    @DisplayName("Should be error if 2 digits in CVC/CVV field when credit pay")
    void shouldBeErrorIf2DigitsInCVCWhenCredit() {
        var mainPage = new MainPage();
        creditPage = mainPage.tourCreditPayment();
        var approvedCard = DataGenerator.getApprovedCardInfo();
        var creditPaymentCyrillicOwner = creditPage.creditPaymentCVCWrongFormat(String.valueOf(DataGenerator.getApprovedCardInfo()),
                DataGenerator.generateValidMonth(),
                String.valueOf(DataGenerator.generateValidYear("yy")),
                DataGenerator.generateValidOwnerName(),
                DataGenerator.generate2DigitsNumber(),
                "Неверный формат");
    }
}