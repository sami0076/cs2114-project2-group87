import student.TestCase;

public class BlackjackGUITest extends TestCase {
    private BlackjackGUI gui;

    public void setUp() {
        gui = new BlackjackGUI();
    }

    public void tearDown() {
        if (gui != null) {
            gui.dispose();
        }
    }

    public void testInitialState() {
        assertNotNull(gui);
        assertEquals("Blackjack", gui.getTitle());
        assertTrue(gui.getWidth() > 0);
        assertTrue(gui.getHeight() > 0);
    }
}
