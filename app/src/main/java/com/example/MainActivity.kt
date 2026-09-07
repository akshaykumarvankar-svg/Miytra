package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MityraViewModel
import com.example.ui.MityraViewModelFactory
import com.example.ui.ScreenTab
import com.example.ui.components.FakeCallOverlay
import com.example.ui.components.MityraBottomNav
import com.example.ui.components.MityraTopBar
import com.example.ui.components.SosEmergencyModal
import com.example.ui.components.VerificationBadgeModal
import com.example.ui.screens.BookingFlowScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CompanionDetailScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.MyBookingsScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SafetyCenterScreen
import com.example.ui.screens.SelfRegisterScreen
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MityraTheme {
                val viewModel: MityraViewModel = viewModel(
                    factory = MityraViewModelFactory(applicationContext)
                )
                MityraApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MityraApp(viewModel: MityraViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val companions by viewModel.filteredCompanions.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedGender by viewModel.selectedGender.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val maxHourlyRate by viewModel.maxHourlyRate.collectAsStateWithLifecycle()

    val selectedCompanion by viewModel.selectedCompanion.collectAsStateWithLifecycle()
    val bookingCompanion by viewModel.bookingCompanion.collectAsStateWithLifecycle()
    val chatCompanion by viewModel.chatCompanion.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val activeBookings by viewModel.activeBookings.collectAsStateWithLifecycle()

    val verifiedBadgeCompanion by viewModel.verifiedBadgeCompanion.collectAsStateWithLifecycle()
    val isSosModalOpen by viewModel.isSosModalOpen.collectAsStateWithLifecycle()
    val isFakeCallActive by viewModel.isFakeCallActive.collectAsStateWithLifecycle()

    val isPaymentScreenOpen by viewModel.isPaymentScreenOpen.collectAsStateWithLifecycle()
    val isPaymentSuccess by viewModel.isPaymentSuccess.collectAsStateWithLifecycle()
    val isSelfRegisterOpen by viewModel.isSelfRegisterOpen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val bookingEventType by viewModel.bookingEventType.collectAsStateWithLifecycle()
    val bookingDate by viewModel.bookingDate.collectAsStateWithLifecycle()
    val bookingTimeSlot by viewModel.bookingTimeSlot.collectAsStateWithLifecycle()
    val bookingDurationHours by viewModel.bookingDurationHours.collectAsStateWithLifecycle()
    val bookingVenueAddress by viewModel.bookingVenueAddress.collectAsStateWithLifecycle()
    val bookingSpecialNotes by viewModel.bookingSpecialNotes.collectAsStateWithLifecycle()
    val appliedCoupon by viewModel.appliedCoupon.collectAsStateWithLifecycle()
    val discountAmount by viewModel.discountAmount.collectAsStateWithLifecycle()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsStateWithLifecycle()
    val selectedUpiApp by viewModel.selectedUpiApp.collectAsStateWithLifecycle()
    val cardNumber by viewModel.cardNumber.collectAsStateWithLifecycle()
    val cardExpiry by viewModel.cardExpiry.collectAsStateWithLifecycle()
    val cardCvv by viewModel.cardCvv.collectAsStateWithLifecycle()

    // Handle back button behavior
    val isFullScreenFlowOpen = isSelfRegisterOpen || chatCompanion != null || isPaymentScreenOpen || bookingCompanion != null || selectedCompanion != null
    BackHandler(enabled = isFullScreenFlowOpen) {
        when {
            isSelfRegisterOpen -> viewModel.closeSelfRegister()
            chatCompanion != null -> viewModel.closeChat()
            isPaymentScreenOpen -> viewModel.closePaymentScreen()
            bookingCompanion != null -> viewModel.closeBookingFlow()
            selectedCompanion != null -> viewModel.selectCompanion(null)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MityraDarkBackground),
        containerColor = MityraDarkBackground,
        topBar = {
            if (!isFullScreenFlowOpen) {
                MityraTopBar(
                    selectedCity = selectedCity,
                    onCitySelected = { viewModel.setCity(it) },
                    onSosClicked = { viewModel.openSosModal() }
                )
            }
        },
        bottomBar = {
            if (!isFullScreenFlowOpen) {
                MityraBottomNav(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    activeBookingsCount = activeBookings.size
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Navigation & Screens
            when {
                isSelfRegisterOpen -> {
                    SelfRegisterScreen(
                        existingProfile = userProfile,
                        onSaveProfile = { profile, registerAsCompanion ->
                            viewModel.saveUserProfile(profile, registerAsCompanion)
                        },
                        onClose = { viewModel.closeSelfRegister() }
                    )
                }
                chatCompanion != null -> {
                    val comp = chatCompanion!!
                    ChatScreen(
                        companion = comp,
                        messages = chatMessages,
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onBack = { viewModel.closeChat() },
                        onBookClick = {
                            viewModel.closeChat()
                            viewModel.startBookingFlow(comp)
                        }
                    )
                }
                isPaymentScreenOpen && bookingCompanion != null -> {
                    val comp = bookingCompanion!!
                    PaymentScreen(
                        companion = comp,
                        eventType = bookingEventType,
                        date = bookingDate,
                        timeSlot = bookingTimeSlot,
                        durationHours = bookingDurationHours,
                        venueAddress = bookingVenueAddress,
                        appliedCoupon = appliedCoupon,
                        discountAmount = discountAmount,
                        selectedPaymentMethod = selectedPaymentMethod,
                        onPaymentMethodChange = { viewModel.setPaymentMethod(it) },
                        selectedUpiApp = selectedUpiApp,
                        onUpiAppChange = { viewModel.setUpiApp(it) },
                        cardNumber = cardNumber,
                        onCardNumberChange = { viewModel.setCardNumber(it) },
                        cardExpiry = cardExpiry,
                        onCardExpiryChange = { viewModel.setCardExpiry(it) },
                        cardCvv = cardCvv,
                        onCardCvvChange = { viewModel.setCardCvv(it) },
                        onApplyCoupon = { viewModel.applyCoupon(it) },
                        isPaymentSuccess = isPaymentSuccess,
                        onCompletePayment = { viewModel.confirmPayment() },
                        onClose = { viewModel.closePaymentScreen() },
                        onGoToChat = {
                            viewModel.closePaymentScreen()
                            viewModel.closeBookingFlow()
                            viewModel.openChat(comp)
                        },
                        onGoToBookings = {
                            viewModel.closePaymentScreen()
                            viewModel.closeBookingFlow()
                            viewModel.selectTab(ScreenTab.BOOKINGS)
                        }
                    )
                }
                bookingCompanion != null -> {
                    val comp = bookingCompanion!!
                    BookingFlowScreen(
                        companion = comp,
                        eventType = bookingEventType,
                        onEventTypeChange = { viewModel.setBookingEventType(it) },
                        bookingDate = bookingDate,
                        onDateChange = { viewModel.setBookingDate(it) },
                        timeSlot = bookingTimeSlot,
                        onTimeSlotChange = { viewModel.setBookingTimeSlot(it) },
                        durationHours = bookingDurationHours,
                        onDurationChange = { viewModel.setBookingDurationHours(it) },
                        venueAddress = bookingVenueAddress,
                        onVenueChange = { viewModel.setBookingVenueAddress(it) },
                        specialNotes = bookingSpecialNotes,
                        onNotesChange = { viewModel.setBookingSpecialNotes(it) },
                        onClose = { viewModel.closeBookingFlow() },
                        onProceedToPayment = { viewModel.proceedToPayment() }
                    )
                }
                selectedCompanion != null -> {
                    val comp = selectedCompanion!!
                    CompanionDetailScreen(
                        companion = comp,
                        onBack = { viewModel.selectCompanion(null) },
                        onBookClick = { viewModel.startBookingFlow(comp) },
                        onChatClick = { viewModel.openChat(comp) },
                        onVerifyClick = { viewModel.openVerifiedBadgeDetails(comp) }
                    )
                }
                else -> {
                    when (currentTab) {
                        ScreenTab.EXPLORE -> {
                            ExploreScreen(
                                companions = companions,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                selectedCategory = selectedCategory,
                                onCategorySelected = { viewModel.selectCategory(it) },
                                selectedGender = selectedGender,
                                onGenderSelected = { viewModel.setGender(it) },
                                maxHourlyRate = maxHourlyRate,
                                onMaxHourlyRateSelected = { viewModel.setMaxHourlyRate(it) },
                                onCompanionClick = { viewModel.selectCompanion(it) },
                                onBookClick = { viewModel.startBookingFlow(it) },
                                onChatClick = { viewModel.openChat(it) },
                                onVerifyClick = { viewModel.openVerifiedBadgeDetails(it) },
                                onSelfRegisterClick = { viewModel.openSelfRegister() }
                            )
                        }
                        ScreenTab.BOOKINGS -> {
                            MyBookingsScreen(
                                bookings = activeBookings,
                                onChatWithCompanion = { companionId ->
                                    val companion = companions.firstOrNull { it.id == companionId }
                                        ?: viewModel.allCompanions.value.firstOrNull { it.id == companionId }
                                    if (companion != null) {
                                        viewModel.openChat(companion)
                                    }
                                },
                                onSosClick = { viewModel.openSosModal() },
                                onExploreClick = { viewModel.selectTab(ScreenTab.EXPLORE) }
                            )
                        }
                        ScreenTab.SAFETY -> {
                            SafetyCenterScreen(
                                onTriggerSos = { viewModel.openSosModal() },
                                onTriggerFakeCall = { viewModel.triggerFakeCall() }
                            )
                        }
                        ScreenTab.PROFILE -> {
                            ProfileScreen(
                                userProfile = userProfile,
                                onEditOrRegisterClick = { viewModel.openSelfRegister() }
                            )
                        }
                    }
                }
            }

            // Overlays and Modals
            if (isSosModalOpen) {
                SosEmergencyModal(
                    onDismiss = { viewModel.closeSosModal() },
                    onTriggerFakeCall = {
                        viewModel.closeSosModal()
                        viewModel.triggerFakeCall()
                    }
                )
            }

            verifiedBadgeCompanion?.let { comp ->
                VerificationBadgeModal(
                    companion = comp,
                    onDismiss = { viewModel.closeVerifiedBadgeDetails() }
                )
            }

            if (isFakeCallActive) {
                FakeCallOverlay(
                    onDismiss = { viewModel.dismissFakeCall() }
                )
            }
        }
    }
}
