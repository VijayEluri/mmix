2.6.21.3 is not a full source tree, it is either a stable release patch or 
base kernel release patch. it is more likely to be the stable release patch of 2.6.21

2.6.24.3 is a full source tree.



1. Re-install vim to support cscope, by default, it does not support cscope.
tar -jxvf vim-7.1.tar.bz2 
cd vim71/
./configure --enable-cscope
make
vim --version | grep scope
sudo make install

following command
which vim
can check where the vim is installed. in my case, it is /usr/local/bin/vim
following command
vim --version | grep scope
check whether cscope is enabled successfully.


2. generate ctags is easy, 
ctags -R
output is a file named tags in current directory.

ctags -L /home/eddie/linux/cscope/cscope.files

 :set tag=tag_file_path
	if you do not start the vim from the folder where the tags file are located.
 :set tag=/home/eddie/linux/linux-2.6.24.3/tags

3. add ctags index file to vim is easy.
 :set tag=tag_file_path
	if you do not start the vim from the folder where the tags file are located.
 :set tag=/home/eddie/linux/linux-2.6.24.3/tags
 :tag fuction_name

vim setting
 	:set cindent
	:set shiftwidth=4
	:set tabstop=4

4. cscope is already installed in ubuntu, 
	-path "$LNX/drivers*" -prune -o                                       \

current working directory is /home/eddie/linux (PWD=/home/eddie/linux)

mkdir cscope

LNX=/home/eddie/linux/linux-2.6
    cd / 	
    find  $LNX                                                                \
	-path "$LNX/arch/*" ! -path "$LNX/arch/x86*" -prune -o               \
	-path "$LNX/include/asm-*" ! -path "$LNX/include/asm-x86*" -prune -o \
	-path "$LNX/tmp*" -prune -o                                           \
	-path "$LNX/Documentation*" -prune -o                                 \
	-path "$LNX/scripts*" -prune -o                                       \
	-path "$LNX/debian*" -prune -o                          \
	-path "$LNX/crypto*" -prune -o                          \
	-path "$LNX/net*" -prune -o                             \
	-path "$LNX/samples*" -prune -o                         \
	-path "$LNX/sound*" -prune -o                           \
        -name "*.[chxsS]" -print >/home/eddie/linux/cscope/cscope.files

LNX=/home/eddie/linux/linux-2.6
find  $LNX   -path "$LNX/arch/*" ! -path "$LNX/arch/x86*"-prune -o               		\
	-path "$LNX/include/asm-*" ! -path "$LNX/include/asm-x86*" ! -path "$LNX/include/asm-generic*" -prune -o \
	-path "$LNX/tmp*" -prune -o                             \
	-path "$LNX/Documentation*" -prune -o                   \
	-path "$LNX/scripts*" -prune -o                         \
	-path "$LNX/debian*" -prune -o                          \
	-path "$LNX/crypto*" -prune -o                          \
	-path "$LNX/net*" -prune -o                             \
	-path "$LNX/samples*" -prune -o                         \
	-path "$LNX/sound*" -prune -o                           \
        -name "*.[chxsS]" -print >/home/eddie/linux/cscope/cscope.files

find  $LNX   -path "$LNX/arch/*" ! -path "$LNX/arch/x86*"-prune -o \
	-path "$LNX/tmp*" -prune -o                             \
	-path "$LNX/Documentation*" -prune -o                   \
	-path "$LNX/scripts*" -prune -o                         \
	-path "$LNX/debian*" -prune -o                          \
	-path "$LNX/crypto*" -prune -o                          \
	-path "$LNX/net*" -prune -o                             \
	-path "$LNX/samples*" -prune -o                         \
	-path "$LNX/sound*" -prune -o      
-name "*.[chxsS]" -print >/home/eddie/linux/cscope/cscope.files

LNX=/home/eddie/linux/linux-2.6.24.3
find  $LNX                                                                \
	--path "$LNX/arch/*" ! -path "$LNX/arch/x86*"-prune -o \
	-path "$LNX/include/asm-*" ! -path "$LNX/include/asm-x86*" ! -path "$LNX/include/asm-generic*" -prune -o \
	-path "$LNX/tmp*" -prune -o                                           \
	-path "$LNX/Documentation*" -prune -o                                 \
	-path "$LNX/scripts*" -prune -o                                       \
	-path "$LNX/debian*" -prune -o                                       \
	-path "$LNX/crypto*" -prune -o                                       \
	-path "$LNX/lib*" -prune -o                                       \
	-path "$LNX/mm*" -prune -o                                       \
	-path "$LNX/net*" -prune -o                                       \
	-path "$LNX/samples*" -prune -o                                       \
	-path "$LNX/security*" -prune -o                                       \
	-path "$LNX/sound*" -prune -o                                       \
        -name "*.[chxsS]" -print >/home/eddie/linux/cscope/cscope.files

cd cscope
	cscope -b -q -k
ctags -L/home/eddie/linux/cscope/cscope.files
execute following in vim
 	:cscope add /home/eddie/linux/cscope/cscope.out
(we need to configure this to be loaded when we start vim, otherwise, you need to execute it everytime you restart the vim.)
 	
	USAGE   :cs find {querytype} {name}

            {querytype} corresponds to the actual cscope line
            interface numbers as well as default nvi commands:

           0 or s: Find this C symbol
                1 or g: Find this definition
                2 or d: Find functions called by this function
                3 or c: Find functions calling this function
                4 or t: Find this text string
                6 or e: Find this egrep pattern
                7 or f: Find this file
                8 or i: Find files #including this file

        EXAMPLES
            :cscope find c vim_free
            :cscope find 3 vim_free

:cs find c request_irq

output is too long and I can not scroll back to the beginning.

If I use cscope directly, I do not know how to start new round of query.

 If the search is successful, any of these single-character commands can
       be used:

       0-9a-zA-Z
              Edit the file referenced by the given line number.

       <Space>
              Display next set of matching lines.

       <Tab>  Alternate between the menu and the list of matching lines	--important

       <Up>   Move to the previous menu item (if the cursor is in the menu) or
              move  to  the  previous  matching  line (if the cursor is in the
              matching line list.)

       <Down> Move to the next menu item (if the cursor is  in  the  menu)  or
              move to the next matching line (if the cursor is in the matching
              line list.)

       +      Display next set of matching lines.

       -      Display previous set of matching lines.

       ^e     Edit displayed files in order.

       >      Write the displayed list of lines to a file.

       >>     Append the displayed list of lines to a file.

       <      Read lines from a file that is in symbol reference format  (cre‐
              ated by > or >>), just like the -F option.

       ^      Filter all lines through a shell command and display the result‐
              ing lines, replacing the lines that were already there.

       |      Pipe all lines to a  shell  command  and  display  them  without
              changing them.

        At any time these single-character commands can also be used:

       <Return>
              Move to next input field.

       ^n     Move to next input field.

       ^p     Move to previous input field.

       ^y     Search with the last text typed.

       ^b     Move to previous input field and search pattern.

       ^f     Move to next input field and search pattern.

       ^c     Toggle  ignore/use  letter  case  when searching. (When ignoring
              letter  case,  search  for  ‘‘FILE’’  will  match  ‘‘File’’  and
              ‘‘file’’.)

       ^r     Rebuild the cross-reference.

       !      Start an interactive shell (type ^d to return to cscope).

       ^l     Redraw the screen.

       ?      Give help information about cscope commands.

       ^d     Exit cscope.

       NOTE: If the first character of the text to be searched for matches one
       of the above commands, escape it by typing a (backslash) first.
 	
	Substituting new text for old text

       After the text to be changed has been typed, cscope will prompt for the
       new  text,  and then it will display the lines containing the old text.
       Select the lines to be changed with these single-character commands:

       0-9a-zA-Z
              Mark or unmark the line to be changed.

       *      Mark or unmark all displayed lines to be changed.

       <Space>
              Display next set of lines.

       +      Display next set of lines.

       -      Display previous set of lines.

       a      Mark or unmark all lines to be changed.

       ^d     Change the marked lines and exit.

       <Esc>  Exit without changing the marked lines.

       !      Start an interactive shell (type ^d to return to cscope).

       ^l     Redraw the screen.

       ?      Give help information about cscope commands.

       Special keys

       If your terminal has arrow keys that work in vi, you can  use  them  to
       move around the input fields. The up-arrow key is useful to move to the
       previous input field instead of using the <Tab> key repeatedly. If  you
       have  <CLEAR>, <NEXT>, or <PREV> keys they will act as the ^l, +, and -
       commands, respectively.


to research how the keyboard works
1. 
more /proc/interrupts
know the name "i8042"

2.
for request_irq
search and grep with i8042 (^ to be in the mode), the following is the result.

0 i8042.c i8042_check_aux  640 if (request_irq(I8042_AUX_IRQ, i8042_aux_test_irq, IRQF_SHARED,
1 i8042.c i8042_setup_aux 1094 error = request_irq(I8042_AUX_IRQ, i8042_interrupt, IRQF_SHARED,
2 i8042.c i8042_setup_kbd 1120 error = request_irq(I8042_KBD_IRQ, i8042_interrupt, IRQF_SHARED,

# define I8042_KBD_IRQ  1
# define I8042_KBD_IRQ  1
# define I8042_AUX_IRQ  12

3. in the kernel initialization phase, request_irq is invoked to regist the interrupt service handler.
//may be initialized here.
static int __devinit i8042_setup_kbd(void)
{
        int error;

        error = i8042_create_kbd_port();
        if (error)
                return error;

        error = request_irq(I8042_KBD_IRQ, i8042_interrupt, IRQF_SHARED,
                            "i8042", i8042_platform_device);
        if (error)
                goto err_free_port;

        error = i8042_enable_kbd_port();
        if (error)
                goto err_free_irq;

        i8042_kbd_irq_registered = 1;
        return 0;

 err_free_irq:
        free_irq(I8042_KBD_IRQ, i8042_platform_device);
 err_free_port:
        i8042_free_kbd_port();
        return error;
}

static int __devinit i8042_setup_aux(void)
{
        int (*aux_enable)(void);
        int error;
        int i;

        if (i8042_check_aux())
                return -ENODEV;

        if (i8042_nomux || i8042_check_mux()) {
                error = i8042_create_aux_port(-1);
                if (error)
                        goto err_free_ports;
                aux_enable = i8042_enable_aux_port;
        } else {
                for (i = 0; i < I8042_NUM_MUX_PORTS; i++) {
                        error = i8042_create_aux_port(i);
                        if (error)
                                goto err_free_ports;
                }
                aux_enable = i8042_enable_mux_ports;
        }

        error = request_irq(I8042_AUX_IRQ, i8042_interrupt, IRQF_SHARED,
                            "i8042", i8042_platform_device);
        if (error)
                goto err_free_ports;

        if (aux_enable())
                goto err_free_irq;

        i8042_aux_irq_registered = 1;
        return 0;

 err_free_irq:
        free_irq(I8042_AUX_IRQ, i8042_platform_device);
 err_free_ports:
        i8042_free_aux_ports();
        return error;

4. also know that keyboard and mouse will share the same controller.it is called multi-plexing



5. data structure

struct serio {
        void *port_data;

        char name[32];
        char phys[32];

        unsigned int manual_bind;

        struct serio_device_id id;

        spinlock_t lock;                /* protects critical sections from port's interrupt handler */

        int (*write)(struct serio *, unsigned char);
        int (*open)(struct serio *);
        void (*close)(struct serio *);
        int (*start)(struct serio *);
        void (*stop)(struct serio *);

        struct serio *parent, *child;
        unsigned int depth;             /* level of nesting in serio hierarchy */

        struct serio_driver *drv;       /* accessed from interrupt, must be protected by serio->lock and serio->sem */
        struct mutex drv_mutex;         /* protects serio->drv so attributes can pin driver */

        struct device dev;
        unsigned int registered;        /* port has been fully registered with driver core */

        struct list_head node;
};

#define to_serio_port(d)        container_of(d, struct serio, dev)

struct serio_driver {
        void *private;
        char *description;

        struct serio_device_id *id_table;
        unsigned int manual_bind;

        void (*write_wakeup)(struct serio *);
        irqreturn_t (*interrupt)(struct serio *, unsigned char, unsigned int);
        int  (*connect)(struct serio *, struct serio_driver *drv);
        int  (*reconnect)(struct serio *);
        void (*disconnect)(struct serio *);
        void (*cleanup)(struct serio *);

        struct device_driver driver;
};



in drivers/input/serio/i8042-io.h
/*
 * This is in 50us units, the time we wait for the i8042 to react. This
 * has to be long enough for the i8042 itself to timeout on sending a byte
 * to a non-existent mouse.
 */

#define I8042_CTL_TIMEOUT       10000

/*
 * Status register bits.
 */

#define I8042_STR_PARITY        0x80
#define I8042_STR_TIMEOUT       0x40
#define I8042_STR_AUXDATA       0x20
#define I8042_STR_KEYLOCK       0x10
#define I8042_STR_CMDDAT        0x08
#define I8042_STR_MUXERR        0x04
#define I8042_STR_IBF           0x02
#define I8042_STR_OBF           0x01

/*
 * Control register bits.
 */

#define I8042_CTR_KBDINT        0x01
#define I8042_CTR_AUXINT        0x02
#define I8042_CTR_IGNKEYLOCK    0x08
#define I8042_CTR_KBDDIS        0x10
#define I8042_CTR_AUXDIS        0x20
#define I8042_CTR_XLATE         0x40

/*
 * Return codes.
 */

#define I8042_RET_CTL_TEST      0x55

/*
 * Expected maximum internal i8042 buffer size. This is used for flushing
 * the i8042 buffers.
 */

#ifdef CONFIG_HOTPLUG
#define __devinit
#define __devinitdata
#define __devexit
#define __devexitdata
#else
#define __devinit __init
#define __devinitdata __initdata
#define __devexit __exit
#define __devexitdata __exitdata
#endif

6.
we define the __devinit for the convenience of hotplug.
__init only decide whether the code generated for the function will be put into the initial text segment/section.
__initcall will decide whether the function will be invoked in the kernel initializtion.

struct i8042_port {
        struct serio *serio;
        int irq;
        unsigned char exists;
        signed char mux;
};

struct serio {
        void *port_data;

        char name[32];
        char phys[32];

        unsigned int manual_bind;

        struct serio_device_id id;

        spinlock_t lock;                /* protects critical sections from port's interrupt handler */

        int (*write)(struct serio *, unsigned char);
        int (*open)(struct serio *);
        void (*close)(struct serio *);
        int (*start)(struct serio *);
        void (*stop)(struct serio *);

        struct serio *parent, *child;
        unsigned int depth;             /* level of nesting in serio hierarchy */

        struct serio_driver *drv;       /* accessed from interrupt, must be protected by serio->lock and serio->sem */
        struct mutex drv_mutex;         /* protects serio->drv so attributes can pin driver */

        struct device dev;
        unsigned int registered;        /* port has been fully registered with driver core */

        struct list_head node;
};
#define to_serio_port(d)        container_of(d, struct serio, dev)

struct serio_driver {
        void *private;
        char *description;

        struct serio_device_id *id_table;
        unsigned int manual_bind;

        void (*write_wakeup)(struct serio *);
        irqreturn_t (*interrupt)(struct serio *, unsigned char, unsigned int);
        int  (*connect)(struct serio *, struct serio_driver *drv);
        int  (*reconnect)(struct serio *);
        void (*disconnect)(struct serio *);
        void (*cleanup)(struct serio *);

        struct device_driver driver;
};





static struct platform_driver i8042_driver = {
        .driver         = {
                .name   = "i8042",
                .owner  = THIS_MODULE,
        },
        .probe          = i8042_probe,
        .remove         = __devexit_p(i8042_remove),
        .shutdown       = i8042_shutdown,
#ifdef CONFIG_PM
        .suspend        = i8042_suspend,
        .resume         = i8042_resume,
#endif
};

--key entry point

platform_device.h - generic, centralized driver model
struct platform_device {
        const char      * name;
        int             id;
        struct device   dev;
        u32             num_resources;
        struct resource * resource;
};   

struct platform_driver {
        int (*probe)(struct platform_device *);
        int (*remove)(struct platform_device *);
        void (*shutdown)(struct platform_device *);
        int (*suspend)(struct platform_device *, pm_message_t state);
        int (*suspend_late)(struct platform_device *, pm_message_t state);
        int (*resume_early)(struct platform_device *);
        int (*resume)(struct platform_device *);
        struct device_driver driver;
};

struct device_driver {
        const char              * name;
        struct bus_type         * bus;

        struct kobject          kobj;
        struct klist            klist_devices;
        struct klist_node       knode_bus;
 
        struct module           * owner;
        const char              * mod_name;     /* used for built-in modules */
        struct module_kobject   * mkobj;

        int     (*probe)        (struct device * dev); 
        int     (*remove)       (struct device * dev);
        void    (*shutdown)     (struct device * dev);
        int     (*suspend)      (struct device * dev, pm_message_t state);
        int     (*resume)       (struct device * dev);
};

7.
/*
 * i8042_interrupt() is the most important function in this driver -
 * it handles the interrupts from the i8042, and sends incoming bytes
 * to the upper layers.
 */

static irqreturn_t i8042_interrupt(int irq, void *dev_id)
{
        struct i8042_port *port;
        unsigned long flags;
        unsigned char str, data;
        unsigned int dfl;
        unsigned int port_no;
        int ret = 1;

        spin_lock_irqsave(&i8042_lock, flags);
        str = i8042_read_status();
        if (unlikely(~str & I8042_STR_OBF)) {
                spin_unlock_irqrestore(&i8042_lock, flags);
                if (irq) dbg("Interrupt %d, without any data", irq);
                ret = 0;
                goto out;
        }
        data = i8042_read_data();
        spin_unlock_irqrestore(&i8042_lock, flags);

        if (i8042_mux_present && (str & I8042_STR_AUXDATA)) {
                static unsigned long last_transmit;
                static unsigned char last_str;

                dfl = 0;
                if (str & I8042_STR_MUXERR) {
                        dbg("MUX error, status is %02x, data is %02x", str, data);
                      switch (data) {
                                default:
                                        if (time_before(jiffies, last_transmit + HZ/10)) {
                                                str = last_str;
                                                break;
                                        }
                                        /* fall through - report timeout */
                                case 0xfc:
                                case 0xfd: 
                                case 0xfe: dfl = SERIO_TIMEOUT; data = 0xfe; break;
                                case 0xff: dfl = SERIO_PARITY;  data = 0xfe; break;
                        }
                }

                port_no = I8042_MUX_PORT_NO + ((str >> 6) & 3);
                last_str = str; 
                last_transmit = jiffies;
        } else {

                dfl = ((str & I8042_STR_PARITY) ? SERIO_PARITY : 0) |
                      ((str & I8042_STR_TIMEOUT) ? SERIO_TIMEOUT : 0);

                port_no = (str & I8042_STR_AUXDATA) ?
                                I8042_AUX_PORT_NO : I8042_KBD_PORT_NO;
        }

        port = &i8042_ports[port_no];

        dbg("%02x <- i8042 (interrupt, %d, %d%s%s)",
            data, port_no, irq,
            dfl & SERIO_PARITY ? ", bad parity" : "",
            dfl & SERIO_TIMEOUT ? ", timeout" : "");

        if (unlikely(i8042_suppress_kbd_ack))
                if (port_no == I8042_KBD_PORT_NO &&
                    (data == 0xfa || data == 0xfe)) {
                        i8042_suppress_kbd_ack--;
                        goto out;
                }

        if (likely(port->exists))
                serio_interrupt(port->serio, data, dfl);

 out:
        return IRQ_RETVAL(ret);
}


8.

irqreturn_t serio_interrupt(struct serio *serio,
                unsigned char data, unsigned int dfl) 
{
        unsigned long flags;
        irqreturn_t ret = IRQ_NONE;

        spin_lock_irqsave(&serio->lock, flags);

        if (likely(serio->drv)) {
                ret = serio->drv->interrupt(serio, data, dfl);
        } else if (!dfl && serio->registered) {
                serio_rescan(serio);
                ret = IRQ_HANDLED;
        }

        spin_unlock_irqrestore(&serio->lock, flags);

	        return ret;
}

9 ret = serio->drv->interrupt(serio, data, dfl);
it is still hard to find out when and where the funtion pointer is initialzied and what is the result.:

10. cs find t _interrupt( 	
 4    387  /home/eddie/linux/linux-2.6.24.3/drivers/input/keyboard/hil_kbd.c <<<unknown>>>
             .interrupt
   5    418  /home/eddie/linux/linux-2.6.24.3/drivers/input/mouse/hil_ptr.c <<<unknown>>>
             .interrupt
   6    274  /home/eddie/linux/linux-2.6.24.3/drivers/input/serio/i8042.c <<<unknown>>>
              * i8042_stop() marks serio port as non-existing so i8042_interrupt
static struct serio_driver hil_kbd_serio_drv = {
        .driver         = {
                .name   = "hil_kbd",
        },
        .description    = "HP HIL keyboard driver",
        .id_table       = hil_kbd_ids,
        .connect        = hil_kbd_connect,
        .disconnect     = hil_kbd_disconnect,
        .interrupt      = hil_kbd_interrupt
};

static irqreturn_t hil_kbd_interrupt(struct serio *serio,
                                unsigned char data, unsigned int flags)
{
        struct hil_kbd *kbd;
        hil_packet packet;
        int idx;

        kbd = serio_get_drvdata(serio);
        BUG_ON(kbd == NULL);

        if (kbd->idx4 >= (HIL_KBD_MAX_LENGTH * sizeof(hil_packet))) {
                hil_kbd_process_err(kbd);
                return IRQ_HANDLED;
        }
        idx = kbd->idx4/4;
        if (!(kbd->idx4 % 4))
                kbd->data[idx] = 0;
        packet = kbd->data[idx];
        packet |= ((hil_packet)data) << ((3 - (kbd->idx4 % 4)) * 8);
        kbd->data[idx] = packet;

        /* Records of N 4-byte hil_packets must terminate with a command. */
        if ((++(kbd->idx4)) % 4)
                return IRQ_HANDLED;
        if ((packet & 0xffff0000) != HIL_ERR_INT) {
                hil_kbd_process_err(kbd);
                return IRQ_HANDLED;
        }
        if (packet & HIL_PKT_CMD)
                hil_kbd_process_record(kbd);
        return IRQ_HANDLED;
}
finally i fint the key entry. next step is to connect it with flush_to_ldisc
	`	    
^ to begin grep the result for some keyword. --useful when the result is big and hard to read and you have some rough idea.
new findings:
find . -name '*.c' | xargs egrep -n "\.interrupt"
./drivers/input/keyboard/xtkbd.c:169:   .interrupt      = xtkbd_interrupt,
./drivers/input/keyboard/atkbd.c:1094:  .interrupt      = atkbd_interrupt,

atkbd_interrupt







/*
 * The atkbd control structure
 */

struct atkbd {

        struct ps2dev ps2dev;
        struct input_dev *dev;

        /* Written only during init */
        char name[64];
        char phys[32];

        unsigned short id;
        unsigned char keycode[512];
        unsigned char set;
        unsigned char translated;
        unsigned char extra;
        unsigned char write;
        unsigned char softrepeat;
        unsigned char softraw;
        unsigned char scroll;
        unsigned char enabled;

        /* Accessed only from interrupt */
        unsigned char emul;
        unsigned char resend;
        unsigned char release;
        unsigned long xl_bit;
        unsigned int last;
        unsigned long time;
        unsigned long err_count;

        struct delayed_work event_work;
        unsigned long event_jiffies;
        struct mutex event_mutex;
        unsigned long event_mask;
};

struct input_dev {
        /* private: */
        void *private;  /* do not use */
        /* public: */

        const char *name;
        const char *phys;
        const char *uniq;
        struct input_id id;

        unsigned long evbit[BITS_TO_LONGS(EV_CNT)];
        unsigned long keybit[BITS_TO_LONGS(KEY_CNT)];
        unsigned long relbit[BITS_TO_LONGS(REL_CNT)];
        unsigned long absbit[BITS_TO_LONGS(ABS_CNT)];
        unsigned long mscbit[BITS_TO_LONGS(MSC_CNT)];
        unsigned long ledbit[BITS_TO_LONGS(LED_CNT)];
        unsigned long sndbit[BITS_TO_LONGS(SND_CNT)];
        unsigned long ffbit[BITS_TO_LONGS(FF_CNT)];
        unsigned long swbit[BITS_TO_LONGS(SW_CNT)];

        unsigned int keycodemax;
        unsigned int keycodesize;
        void *keycode;
        int (*setkeycode)(struct input_dev *dev, int scancode, int keycode);
        int (*getkeycode)(struct input_dev *dev, int scancode, int *keycode);
 
        struct ff_device *ff;

        unsigned int repeat_key;
        struct timer_list timer;

        int sync;

        int abs[ABS_MAX + 1];
        int rep[REP_MAX + 1];

        unsigned long key[BITS_TO_LONGS(KEY_CNT)];
        unsigned long led[BITS_TO_LONGS(LED_CNT)]; 
        unsigned long snd[BITS_TO_LONGS(SND_CNT)];
        unsigned long sw[BITS_TO_LONGS(SW_CNT)];

        int absmax[ABS_MAX + 1];
        int absmin[ABS_MAX + 1];
        int absfuzz[ABS_MAX + 1];
        int absflat[ABS_MAX + 1];

        int (*open)(struct input_dev *dev);
        void (*close)(struct input_dev *dev);
        int (*flush)(struct input_dev *dev, struct file *file);
        int (*event)(struct input_dev *dev, unsigned int type, unsigned int code, int value);

        struct input_handle *grab;

        spinlock_t event_lock;
        struct mutex mutex;

        unsigned int users;
        int going_away;

        struct device dev;
        union {                 /* temporarily so while we switching to struct device */
                struct device *dev;
        } cdev;

        struct list_head        h_list;
        struct list_head        node;
};

struct ps2dev {
        struct serio *serio;
 
        /* Ensures that only one command is executing at a time */
        struct mutex cmd_mutex;
        
        /* Used to signal completion from interrupt handler */
        wait_queue_head_t wait;
        
        unsigned long flags;
        unsigned char cmdbuf[6];
        unsigned char cmdcnt;
        unsigned char nak;
};

/*
 * Pass event through all open handles. This function is called with
 * dev->event_lock held and interrupts disabled.
 */
static void input_pass_event(struct input_dev *dev,
                             unsigned int type, unsigned int code, int value)
{
        struct input_handle *handle;

        rcu_read_lock();

        handle = rcu_dereference(dev->grab);
        if (handle)
                handle->handler->event(handle, type, code, value);
        else
                list_for_each_entry_rcu(handle, &dev->h_list, d_node)
                        if (handle->open)
                                handle->handler->event(handle,
                                                        type, code, value);
        rcu_read_unlock();
}

/**
 * struct input_handle - links input device with an input handler 
 * @private: handler-specific data
 * @open: counter showing whether the handle is 'open', i.e. should deliver
 *      events from its device
 * @name: name given to the handle by handler that created it
 * @dev: input device the handle is attached to
 * @handler: handler that works with the device through this handle
 * @d_node: used to put the handle on device's list of attached handles
 * @h_node: used to put the handle on handler's list of handles from which
 *      it gets events
 */
struct input_handle {

        void *private;
        
        int open;
        const char *name;

        struct input_dev *dev;
        struct input_handler *handler;

        struct list_head        d_node;
        struct list_head        h_node;
};
struct input_handler {

        void *private;

        void (*event)(struct input_handle *handle, unsigned int type, unsigned int code, int value);
        int (*connect)(struct input_handler *handler, struct input_dev *dev, const struct input_device_id *id);
        void (*disconnect)(struct input_handle *handle);
        void (*start)(struct input_handle *handle);

        const struct file_operations *fops;
        int minor;
        const char *name;

        const struct input_device_id *id_table;
        const struct input_device_id *blacklist;

        struct list_head        h_list;
        struct list_head        node;
};
when and how the input_handler is initialized? what is the actual value of event function pointer
static struct serio_driver atkbd_drv = {
        .driver         = {
                .name   = "atkbd",
        },
        .description    = DRIVER_DESC,
        .id_table       = atkbd_serio_ids,
        .interrupt      = atkbd_interrupt,
        .connect        = atkbd_connect,
        .reconnect      = atkbd_reconnect,
        .disconnect     = atkbd_disconnect,
        .cleanup        = atkbd_cleanup,
};

serio_set_drvdata
find . -name '*.c' | xargs egrep -n "serio_set_drvdata" | more
./drivers/input/keyboard/atkbd.c:981:   serio_set_drvdata(serio, atkbd);
/*
 * atkbd_connect() is called when the serio module finds an interface
 * that isn't handled yet by an appropriate device driver. We check if
 * there is an AT keyboard out there and if yes, we register ourselves
 * to the input module.
 */

static int atkbd_connect(struct serio *serio, struct serio_driver *drv)
{
        struct atkbd *atkbd;
        struct input_dev *dev;
        int err = -ENOMEM;

        atkbd = kzalloc(sizeof(struct atkbd), GFP_KERNEL);
        dev = input_allocate_device();
        if (!atkbd || !dev)
                goto fail1;

        atkbd->dev = dev;
        ps2_init(&atkbd->ps2dev, serio);
        INIT_DELAYED_WORK(&atkbd->event_work, atkbd_event_work);
        mutex_init(&atkbd->event_mutex);

        switch (serio->id.type) {

                case SERIO_8042_XL:
                        atkbd->translated = 1;
                case SERIO_8042:
                        if (serio->write)
                                atkbd->write = 1;
                        break;
        }

        atkbd->softraw = atkbd_softraw;
        atkbd->softrepeat = atkbd_softrepeat;
        atkbd->scroll = atkbd_scroll;

        if (atkbd->softrepeat)
                atkbd->softraw = 1;

        serio_set_drvdata(serio, atkbd);

        err = serio_open(serio, drv);
        if (err)
                goto fail2;

        if (atkbd->write) {

                if (atkbd_probe(atkbd)) {
                        err = -ENODEV;
                        goto fail3;
                }

                atkbd->set = atkbd_select_set(atkbd, atkbd_set, atkbd_extra);
                atkbd_activate(atkbd);

        } else {
                atkbd->set = 2;
                atkbd->id = 0xab00;
        }

        atkbd_set_keycode_table(atkbd);
        atkbd_set_device_attrs(atkbd);

        err = sysfs_create_group(&serio->dev.kobj, &atkbd_attribute_group);
        if (err)
                goto fail3;

        atkbd_enable(atkbd);

        err = input_register_device(atkbd->dev);
        if (err)
                goto fail4;

        return 0;

 fail4: sysfs_remove_group(&serio->dev.kobj, &atkbd_attribute_group);
 fail3: serio_close(serio);
 fail2: serio_set_drvdata(serio, NULL);
 fail1: input_free_device(dev);
        kfree(atkbd);
        return err;
}

static void atkbd_set_device_attrs(struct atkbd *atkbd)
{
        struct input_dev *input_dev = atkbd->dev;
        int i;

        if (atkbd->extra)
                snprintf(atkbd->name, sizeof(atkbd->name),
                         "AT Set 2 Extra keyboard");
        else
                snprintf(atkbd->name, sizeof(atkbd->name),
                         "AT %s Set %d keyboard",
                         atkbd->translated ? "Translated" : "Raw", atkbd->set);

        snprintf(atkbd->phys, sizeof(atkbd->phys),
                 "%s/input0", atkbd->ps2dev.serio->phys);

        input_dev->name = atkbd->name;
        input_dev->phys = atkbd->phys;
        input_dev->id.bustype = BUS_I8042;
        input_dev->id.vendor = 0x0001;
        input_dev->id.product = atkbd->translated ? 1 : atkbd->set;
        input_dev->id.version = atkbd->id;
        input_dev->event = atkbd_event;
        input_dev->dev.parent = &atkbd->ps2dev.serio->dev;

        input_set_drvdata(input_dev, atkbd);

        input_dev->evbit[0] = BIT_MASK(EV_KEY) | BIT_MASK(EV_REP) |
                BIT_MASK(EV_MSC);

        if (atkbd->write) {
                input_dev->evbit[0] |= BIT_MASK(EV_LED);
                input_dev->ledbit[0] = BIT_MASK(LED_NUML) |
                        BIT_MASK(LED_CAPSL) | BIT_MASK(LED_SCROLLL);
        }

        if (atkbd->extra)
                input_dev->ledbit[0] |= BIT_MASK(LED_COMPOSE) |
                        BIT_MASK(LED_SUSPEND) | BIT_MASK(LED_SLEEP) |
                        BIT_MASK(LED_MUTE) | BIT_MASK(LED_MISC);

        if (!atkbd->softrepeat) {
                input_dev->rep[REP_DELAY] = 250;
                input_dev->rep[REP_PERIOD] = 33;
        }
	input_dev->mscbit[0] = atkbd->softraw ? BIT_MASK(MSC_SCAN) :
                BIT_MASK(MSC_RAW) | BIT_MASK(MSC_SCAN);

        if (atkbd->scroll) {
                input_dev->evbit[0] |= BIT_MASK(EV_REL);
                input_dev->relbit[0] = BIT_MASK(REL_WHEEL) |
                        BIT_MASK(REL_HWHEEL);
                set_bit(BTN_MIDDLE, input_dev->keybit);
        }

        input_dev->keycode = atkbd->keycode;
        input_dev->keycodesize = sizeof(unsigned char);
        input_dev->keycodemax = ARRAY_SIZE(atkbd_set2_keycode);

        for (i = 0; i < 512; i++)
                if (atkbd->keycode[i] && atkbd->keycode[i] < ATKBD_SPECIAL)
                        set_bit(atkbd->keycode[i], input_dev->keybit);
}

static void atkbd_schedule_event_work(struct atkbd *atkbd, int event_bit)
{
        unsigned long delay = msecs_to_jiffies(50);

        if (time_after(jiffies, atkbd->event_jiffies + delay))
                delay = 0;

        atkbd->event_jiffies = jiffies;
        set_bit(event_bit, &atkbd->event_mask);
        wmb();
        schedule_delayed_work(&atkbd->event_work, delay);
}

struct input_dev *input_allocate_device(void)
{
        struct input_dev *dev;

        dev = kzalloc(sizeof(struct input_dev), GFP_KERNEL);
        if (dev) {
                dev->dev.type = &input_dev_type; 
                dev->dev.class = &input_class;
                device_initialize(&dev->dev);
                mutex_init(&dev->mutex);
                spin_lock_init(&dev->event_lock);
                INIT_LIST_HEAD(&dev->h_list);
                INIT_LIST_HEAD(&dev->node);

                __module_get(THIS_MODULE);
        }

        return dev;
}
EXPORT_SYMBOL(input_allocate_device);

input_allocate_device
/**     
 *      device_initialize - init device structure.
 *      @dev:   device.
 *
 *      This prepares the device for use by other layers,
 *      including adding it to the device hierarchy.
 *      It is the first half of device_register(), if called by
 *      that, though it can also be called separately, so one
 *      may use @dev's fields (e.g. the refcount).
 */

void device_initialize(struct device *dev)
{
        kobj_set_kset_s(dev, devices_subsys);
        kobject_init(&dev->kobj);
        klist_init(&dev->klist_children, klist_children_get,
                   klist_children_put);
        INIT_LIST_HEAD(&dev->dma_pools);
        INIT_LIST_HEAD(&dev->node);
        init_MUTEX(&dev->sem);
        spin_lock_init(&dev->devres_lock);
        INIT_LIST_HEAD(&dev->devres_head);
        device_init_wakeup(dev, 0);
        set_dev_node(dev, -1);
}


static int __devinit i8042_create_kbd_port(void)
{
        struct serio *serio;
        struct i8042_port *port = &i8042_ports[I8042_KBD_PORT_NO];

        serio = kzalloc(sizeof(struct serio), GFP_KERNEL);
        if (!serio)
                return -ENOMEM;


        serio->id.type          = i8042_direct ? SERIO_8042 : SERIO_8042_XL;
        serio->write            = i8042_dumbkbd ? NULL : i8042_kbd_write;
        serio->start            = i8042_start;
        serio->stop             = i8042_stop;
        serio->port_data        = port;
        serio->dev.parent       = &i8042_platform_device->dev;
        strlcpy(serio->name, "i8042 KBD port", sizeof(serio->name));
        strlcpy(serio->phys, I8042_KBD_PHYS_DESC, sizeof(serio->phys));

        port->serio = serio;
        port->irq = I8042_KBDj_IRQ;

        return 0;
}
static int serio_bind_driver(struct serio *serio, struct serio_driver *drv)
{
        int error;

        if (serio_match_port(drv->id_table, serio)) {

                serio->dev.driver = &drv->driver;
                if (serio_connect_driver(serio, drv)) {
                        serio->dev.driver = NULL;
                        return -ENODEV;
                }

                error = device_bind_driver(&serio->dev);
                if (error) {
                        printk(KERN_WARNING
                                "serio: device_bind_driver() failed "
                                "for %s (%s) and %s, error: %d\n",
                                serio->phys, serio->name,
                                drv->description, error);
                        serio_disconnect_driver(serio);
                        serio->dev.driver = NULL;
                        return error;
                }
        }
        return 0;

}


/**
 *      n_tty_receive_char      -       perform processing
 *      @tty: terminal device
 *      @c: character
 *
 *      Process an individual character of input received from the driver.
 *      This is serialized with respect to itself by the rules for the
 *      driver above.
 */

static inline void n_tty_receive_char(struct tty_struct *tty, unsigned char c)
{
        unsigned long flags;

        if (tty->raw) {
                put_tty_queue(c, tty);
                return;
        }

        if (tty->stopped && !tty->flow_stopped &&
            I_IXON(tty) && I_IXANY(tty)) {
                start_tty(tty);
                return;
        }

        if (I_ISTRIP(tty))
                c &= 0x7f;
        if (I_IUCLC(tty) && L_IEXTEN(tty))
                c=tolower(c);

        if (tty->closing) {
                if (I_IXON(tty)) {
                        if (c == START_CHAR(tty))
                                start_tty(tty);
                        else if (c == STOP_CHAR(tty))
                                stop_tty(tty);
                }
                return;
        }

        /*
         * If the previous character was LNEXT, or we know that this
 * character is not one of the characters that we'll have to
         * handle specially, do shortcut processing to speed things
         * up.
         */
        if (!test_bit(c, tty->process_char_map) || tty->lnext) {
                finish_erasing(tty);
                tty->lnext = 0;
                if (L_ECHO(tty)) {
                        if (tty->read_cnt >= N_TTY_BUF_SIZE-1) {
                                put_char('\a', tty); /* beep if no space */
                                return;
                        }
                        /* Record the column of first canon char. */
                        if (tty->canon_head == tty->read_head)
                                tty->canon_column = tty->column;
                        echo_char(c, tty);
                }
                if (I_PARMRK(tty) && c == (unsigned char) '\377')
                        put_tty_queue(c, tty);
                put_tty_queue(c, tty);
                return;
        }
                 
        if (c == '\r') {
                if (I_IGNCR(tty))
                        return;
                if (I_ICRNL(tty))
                        c = '\n';
        } else if (c == '\n' && I_INLCR(tty))
                c = '\r';
        if (I_IXON(tty)) {
                if (c == START_CHAR(tty)) {
                        start_tty(tty);
 if (c == START_CHAR(tty)) {
                        start_tty(tty);
                        return;
                }
                if (c == STOP_CHAR(tty)) {
                        stop_tty(tty);
                        return;
                }
        }
        if (L_ISIG(tty)) {
                int signal;
                signal = SIGINT;
                if (c == INTR_CHAR(tty))
                        goto send_signal;
                signal = SIGQUIT;
                if (c == QUIT_CHAR(tty))
                        goto send_signal;
                signal = SIGTSTP;
                if (c == SUSP_CHAR(tty)) {
send_signal:
                        isig(signal, tty, 0);
                        return;
                }
        }
        if (tty->icanon) {
                if (c == ERASE_CHAR(tty) || c == KILL_CHAR(tty) ||
                    (c == WERASE_CHAR(tty) && L_IEXTEN(tty))) {
                        eraser(c, tty);
                        return;
                }
                if (c == LNEXT_CHAR(tty) && L_IEXTEN(tty)) {
                        tty->lnext = 1;
                        if (L_ECHO(tty)) {
                                finish_erasing(tty);
                                if (L_ECHOCTL(tty)) {
                                        put_char('^', tty);
                                        put_char('\b', tty);
                                }
                        }
                        return;
                }
                if (c == REPRINT_CHAR(tty) && L_ECHO(tty) &&
                    L_IEXTEN(tty)) {
                        unsigned long tail = tty->canon_head;

    unsigned long tail = tty->canon_head;

                        finish_erasing(tty);
                        echo_char(c, tty);
                        opost('\n', tty);
                        while (tail != tty->read_head) {
                                echo_char(tty->read_buf[tail], tty);
                                tail = (tail+1) & (N_TTY_BUF_SIZE-1);
                        }
                        return;
                }
                if (c == '\n') {
                        if (L_ECHO(tty) || L_ECHONL(tty)) {
                                if (tty->read_cnt >= N_TTY_BUF_SIZE-1)
                                        put_char('\a', tty);
                                opost('\n', tty);
                        }
                        goto handle_newline;
                }
                if (c == EOF_CHAR(tty)) {
                        if (tty->canon_head != tty->read_head)
                                set_bit(TTY_PUSH, &tty->flags);
                        c = __DISABLED_CHAR;
                        goto handle_newline;
                }
                if ((c == EOL_CHAR(tty)) || 
                    (c == EOL2_CHAR(tty) && L_IEXTEN(tty))) { 
                        /*
                         * XXX are EOL_CHAR and EOL2_CHAR echoed?!?
                         */
                        if (L_ECHO(tty)) {
                                if (tty->read_cnt >= N_TTY_BUF_SIZE-1)
                                        put_char('\a', tty);
                                /* Record the column of first canon char. */
                                if (tty->canon_head == tty->read_head)
                                        tty->canon_column = tty->column;
                                echo_char(c, tty);
                        }
                        /*
                         * XXX does PARMRK doubling happen for
                         * EOL_CHAR and EOL2_CHAR?
                         */
                        if (I_PARMRK(tty) && c == (unsigned char) '\377')
                                put_tty_queue(c, tty);

 put_tty_queue(c, tty);

                handle_newline:
                        spin_lock_irqsave(&tty->read_lock, flags);
                        set_bit(tty->read_head, tty->read_flags);
                        put_tty_queue_nolock(c, tty);
                        tty->canon_head = tty->read_head;
                        tty->canon_data++;
                        spin_unlock_irqrestore(&tty->read_lock, flags);
                        kill_fasync(&tty->fasync, SIGIO, POLL_IN);
                        if (waitqueue_active(&tty->read_wait))
                                wake_up_interruptible(&tty->read_wait);	//-->wake up
                        return;
                }
        }

        finish_erasing(tty);
        if (L_ECHO(tty)) {
                if (tty->read_cnt >= N_TTY_BUF_SIZE-1) {
                        put_char('\a', tty); /* beep if no space */
                        return;
                }
                if (c == '\n')
                        opost('\n', tty);
                else {
                        /* Record the column of first canon char. */
                        if (tty->canon_head == tty->read_head)
                                tty->canon_column = tty->column;
                        echo_char(c, tty);
                }
        }

        if (I_PARMRK(tty) && c == (unsigned char) '\377')
                put_tty_queue(c, tty);

        put_tty_queue(c, tty);
}

who calles n_tty_receive_char
/**
 *      n_tty_receive_buf       -       data receive
 *      @tty: terminal device
 *      @cp: buffer
 *      @fp: flag buffer
 *      @count: characters
 *
 *      Called by the terminal driver when a block of characters has
 *      been received. This function must be called from soft contexts
 *      not from interrupt context. The driver is responsible for making
 *      calls one at a time and in order (or using flush_to_ldisc)
 */

static void n_tty_receive_buf(struct tty_struct *tty, const unsigned char *cp,
                              char *fp, int count)
{
        const unsigned char *p;
        char *f, flags = TTY_NORMAL;
        int     i;
        char    buf[64];
        unsigned long cpuflags;

        if (!tty->read_buf)
                return;

        if (tty->real_raw) {
                spin_lock_irqsave(&tty->read_lock, cpuflags);
                i = min(N_TTY_BUF_SIZE - tty->read_cnt,
                        N_TTY_BUF_SIZE - tty->read_head);
                i = min(count, i);
                memcpy(tty->read_buf + tty->read_head, cp, i);
                tty->read_head = (tty->read_head + i) & (N_TTY_BUF_SIZE-1);
                tty->read_cnt += i;
                cp += i;
                count -= i;

                i = min(N_TTY_BUF_SIZE - tty->read_cnt,
                        N_TTY_BUF_SIZE - tty->read_head);
                i = min(count, i);
                memcpy(tty->read_buf + tty->read_head, cp, i);
                tty->read_head = (tty->read_head + i) & (N_TTY_BUF_SIZE-1);
                tty->read_cnt += i;
                spin_unlock_irqrestore(&tty->read_lock, cpuflags);
        } else {
 for (i=count, p = cp, f = fp; i; i--, p++) {
                        if (f)
                                flags = *f++;
                        switch (flags) {
                        case TTY_NORMAL:
                                n_tty_receive_char(tty, *p);
                                break;
                        case TTY_BREAK:
                                n_tty_receive_break(tty);
                                break;
                        case TTY_PARITY:
                        case TTY_FRAME:
                                n_tty_receive_parity_error(tty, *p);
                                break;
                        case TTY_OVERRUN:
                                n_tty_receive_overrun(tty);
                                break;
                        default:
                                printk("%s: unknown flag %d\n",
                                       tty_name(tty, buf), flags);
                                break;
                        }
                }
                if (tty->driver->flush_chars)
                        tty->driver->flush_chars(tty);
        }

        n_tty_set_room(tty);

        if (!tty->icanon && (tty->read_cnt >= tty->minimum_to_wake)) {
                kill_fasync(&tty->fasync, SIGIO, POLL_IN);
                if (waitqueue_active(&tty->read_wait))
                        wake_up_interruptible(&tty->read_wait);
        }

        /*
         * Check the remaining room for the input canonicalization
         * mode.  We don't want to throttle the driver if we're in
         * canonical mode and don't have a newline yet!
         */
        if (tty->receive_room < TTY_THRESHOLD_THROTTLE) {
                /* check TTY_THROTTLED first so it indicates our state */
                if (!test_and_set_bit(TTY_THROTTLED, &tty->flags) &&
		    tty->driver->throttle)
                        tty->driver->throttle(tty);
        }
}


struct tty_ldisc tty_ldisc_N_TTY = {
        .magic           = TTY_LDISC_MAGIC,
        .name            = "n_tty",
        .open            = n_tty_open,
        .close           = n_tty_close,
        .flush_buffer    = n_tty_flush_buffer,
        .chars_in_buffer = n_tty_chars_in_buffer,
        .read            = read_chan,
        .write           = write_chan,
        .ioctl           = n_tty_ioctl,
        .set_termios     = n_tty_set_termios,
        .poll            = normal_poll,
        .receive_buf     = n_tty_receive_buf,
        .write_wakeup    = n_tty_write_wakeup
};

:cs find t receive_buf 
/**
 *      flush_to_ldisc
 *      @work: tty structure passed from work queue.
 *
 *      This routine is called out of the software interrupt to flush data
 *      from the buffer chain to the line discipline.
 *
 *      Locking: holds tty->buf.lock to guard buffer list. Drops the lock
 *      while invoking the line discipline receive_buf method. The
 *      receive_buf method is single threaded for each tty instance.
 */

static void flush_to_ldisc(struct work_struct *work)
{
        struct tty_struct *tty =
                container_of(work, struct tty_struct, buf.work.work);
        unsigned long   flags;
        struct tty_ldisc *disc;
        struct tty_buffer *tbuf, *head;
        char *char_buf;
        unsigned char *flag_buf;

        disc = tty_ldisc_ref(tty);
        if (disc == NULL)       /*  !TTY_LDISC */
                return;

        spin_lock_irqsave(&tty->buf.lock, flags);
        set_bit(TTY_FLUSHING, &tty->flags);     /* So we know a flush is running */
        head = tty->buf.head;
        if (head != NULL) {
                tty->buf.head = NULL;
                for (;;) {
                        int count = head->commit - head->read;
                        if (!count) {
                                if (head->next == NULL)
                                        break;
				 tbuf = head;
                                head = head->next;
                                tty_buffer_free(tty, tbuf);
                                continue;
                        }
                        /* Ldisc or user is trying to flush the buffers
                           we are feeding to the ldisc, stop feeding the
                           line discipline as we want to empty the queue */
                        if (test_bit(TTY_FLUSHPENDING, &tty->flags))
                                break;
                        if (!tty->receive_room) {
                                schedule_delayed_work(&tty->buf.work, 1);
                                break;
                        }
                        if (count > tty->receive_room)
                                count = tty->receive_room;
                        char_buf = head->char_buf_ptr + head->read;
                        flag_buf = head->flag_buf_ptr + head->read;
                        head->read += count;
                        spin_unlock_irqrestore(&tty->buf.lock, flags);
                        disc->receive_buf(tty, char_buf, flag_buf, count);
                        spin_lock_irqsave(&tty->buf.lock, flags);
                }
                /* Restore the queue head */
                tty->buf.head = head;
        }
        /* We may have a deferred request to flush the input buffer,
           if so pull the chain under the lock and empty the queue */
        if (test_bit(TTY_FLUSHPENDING, &tty->flags)) {
                __tty_buffer_flush(tty);
                clear_bit(TTY_FLUSHPENDING, &tty->flags);
                wake_up(&tty->read_wait);
        }
        clear_bit(TTY_FLUSHING, &tty->flags);
        spin_unlock_irqrestore(&tty->buf.lock, flags);

        tty_ldisc_deref(disc);
}

:cs find c flush_to_ldisc
/**
 *      tty_flip_buffer_push    -       terminal
 *      @tty: tty to push
 *
 *      Queue a push of the terminal flip buffers to the line discipline. This
 *      function must not be called from IRQ context if tty->low_latency is set.
 *
 *      In the event of the queue being busy for flipping the work will be
 *      held off and retried later.
 *
 *      Locking: tty buffer lock. Driver locks in low latency mode.
 */

void tty_flip_buffer_push(struct tty_struct *tty)
{
        unsigned long flags;
        spin_lock_irqsave(&tty->buf.lock, flags);
        if (tty->buf.tail != NULL)
                tty->buf.tail->commit = tty->buf.tail->used;
        spin_unlock_irqrestore(&tty->buf.lock, flags);

        if (tty->low_latency)
                flush_to_ldisc(&tty->buf.work.work);
        else
                schedule_delayed_work(&tty->buf.work, 1);
}


drivers/serial/8250.c
receive_chars(struct uart_8250_port *up, unsigned int *status)
{
        struct tty_struct *tty = up->port.info->tty;
        unsigned char ch, lsr = *status;
        int max_count = 256;
        char flag;

        do {
                ch = serial_inp(up, UART_RX);
                flag = TTY_NORMAL;
                up->port.icount.rx++;

                lsr |= up->lsr_saved_flags;
                up->lsr_saved_flags = 0;

                if (unlikely(lsr & UART_LSR_BRK_ERROR_BITS)) {
                        /*
                         * For statistics only
                         */
                        if (lsr & UART_LSR_BI) {
                                lsr &= ~(UART_LSR_FE | UART_LSR_PE);
                                up->port.icount.brk++;
                                /*
                                 * We do the SysRQ and SAK checking
                                 * here because otherwise the break
                                 * may get masked by ignore_status_mask
                                 * or read_status_mask.
                                 */
                                if (uart_handle_break(&up->port))
                                        goto ignore_char;
                        } else if (lsr & UART_LSR_PE)
                                up->port.icount.parity++;
                        else if (lsr & UART_LSR_FE)
                                up->port.icount.frame++;
                        if (lsr & UART_LSR_OE)
                                up->port.icount.overrun++;

                        /* 
                         * Mask off conditions which should be ignored.
                         */ 
                        lsr &= up->port.read_status_mask;

                        if (lsr & UART_LSR_BI) {
                                DEBUG_INTR("handling break....");
                                flag = TTY_BREAK;
                        } else if (lsr & UART_LSR_PE)
                                flag = TTY_PARITY;
                        else if (lsr & UART_LSR_FE)
                                flag = TTY_FRAME;
                }
                if (uart_handle_sysrq_char(&up->port, ch))
                        goto ignore_char;

                uart_insert_char(&up->port, lsr, UART_LSR_OE, ch, flag);

        ignore_char:
                lsr = serial_inp(up, UART_LSR);
        } while ((lsr & UART_LSR_DR) && (max_count-- > 0));
        spin_unlock(&up->port.lock);
        tty_flip_buffer_push(tty);
        spin_lock(&up->port.lock);
        *status = lsr;
}

--same file
/*
 * This handles the interrupt from one port.
 */
static inline void
serial8250_handle_port(struct uart_8250_port *up)
{
        unsigned int status;
        unsigned long flags;

        spin_lock_irqsave(&up->port.lock, flags);

        status = serial_inp(up, UART_LSR);

        DEBUG_INTR("status = %x...", status);

        if (status & UART_LSR_DR)
                receive_chars(up, &status);
        check_modem_status(up);
        if (status & UART_LSR_THRE)
                transmit_chars(up);

        spin_unlock_irqrestore(&up->port.lock, flags);
}
/*
 * This is the serial driver's interrupt routine.
 *
 * Arjan thinks the old way was overly complex, so it got simplified.
 * Alan disagrees, saying that need the complexity to handle the weird
 * nature of ISA shared interrupts.  (This is a special exception.)
 *
 * In order to handle ISA shared interrupts properly, we need to check
 * that all ports have been serviced, and therefore the ISA interrupt
 * line has been de-asserted.
 *
 * This means we need to loop through all ports. checking that they
 * don't have an interrupt pending.
 */
static irqreturn_t serial8250_interrupt(int irq, void *dev_id)
{
        struct irq_info *i = dev_id;
        struct list_head *l, *end = NULL;
        int pass_counter = 0, handled = 0;

        DEBUG_INTR("serial8250_interrupt(%d)...", irq);

        spin_lock(&i->lock);

        l = i->head;
        do {
                struct uart_8250_port *up;
                unsigned int iir;

                up = list_entry(l, struct uart_8250_port, list);

                iir = serial_in(up, UART_IIR);
                if (!(iir & UART_IIR_NO_INT)) {
                        serial8250_handle_port(up);

                        handled = 1;

                        end = NULL;
                } else if (up->port.iotype == UPIO_DWAPB &&
                          (iir & UART_IIR_BUSY) == UART_IIR_BUSY) {
                        /* The DesignWare APB UART has an Busy Detect (0x07)
                         * interrupt meaning an LCR write attempt occured while the
                         * UART was busy. The interrupt must be cleared by reading
                         * the UART status register (USR) and the LCR re-written. */
                        unsigned int status;
                        status = *(volatile u32 *)up->port.private_data;
                        serial_out(up, UART_LCR, up->lcr);

                        handled = 1;

                        end = NULL;
                } else if (end == NULL)
                        end = l;

                l = l->next;

                if (l == i->head && pass_counter++ > PASS_LIMIT) {
                        /* If we hit this, we're dead. */
                        printk(KERN_ERR "serial8250: too much work for "
                                "irq%d\n", irq);
                        break;
                }
        } while (l != end);

        spin_unlock(&i->lock);

        DEBUG_INTR("end.\n");

        return IRQ_RETVAL(handled);
}

static int serial_link_irq_chain(struct uart_8250_port *up)
{
        struct irq_info *i = irq_lists + up->port.irq;
        int ret, irq_flags = up->port.flags & UPF_SHARE_IRQ ? IRQF_SHARED : 0;

        spin_lock_irq(&i->lock);

        if (i->head) {
                list_add(&up->list, i->head);
                spin_unlock_irq(&i->lock);

                ret = 0;
        } else {
                INIT_LIST_HEAD(&up->list);
                i->head = &up->list;
                spin_unlock_irq(&i->lock);

                ret = request_irq(up->port.irq, serial8250_interrupt,
                                  irq_flags, "serial", i);
                if (ret < 0)
                        serial_do_unlink(i, up);
        }

        return ret;
}
--> reverse invoking sequence.
static int serial8250_startup(struct uart_port *port)
{
        struct uart_8250_port *up = (struct uart_8250_port *)port;
        unsigned long flags;
        unsigned char lsr, iir;
        int retval;

        up->capabilities = uart_config[up->port.type].flags;
        up->mcr = 0;

        if (up->port.type == PORT_16C950) {
                /* Wake up and initialize UART */
                up->acr = 0;
                serial_outp(up, UART_LCR, 0xBF);
                serial_outp(up, UART_EFR, UART_EFR_ECB);
                serial_outp(up, UART_IER, 0);
                serial_outp(up, UART_LCR, 0);
                serial_icr_write(up, UART_CSR, 0); /* Reset the UART */
                serial_outp(up, UART_LCR, 0xBF);
                serial_outp(up, UART_EFR, UART_EFR_ECB);
                serial_outp(up, UART_LCR, 0);

static struct uart_ops serial8250_pops = {
        .tx_empty       = serial8250_tx_empty,
        .set_mctrl      = serial8250_set_mctrl,
        .get_mctrl      = serial8250_get_mctrl,
        .stop_tx        = serial8250_stop_tx,
        .start_tx       = serial8250_start_tx,
        .stop_rx        = serial8250_stop_rx,
        .enable_ms      = serial8250_enable_ms,
        .break_ctl      = serial8250_break_ctl,
        .startup        = serial8250_startup,
        .shutdown       = serial8250_shutdown,
        .set_termios    = serial8250_set_termios,
        .pm             = serial8250_pm,
        .type           = serial8250_type,
        .release_port   = serial8250_release_port,
        .request_port   = serial8250_request_port,
        .config_port    = serial8250_config_port,
        .verify_port    = serial8250_verify_port,
};

static void __init serial8250_isa_init_ports(void)
{
        struct uart_8250_port *up;
        static int first = 1;
        int i;

        if (!first)
                return;
        first = 0;

        for (i = 0; i < nr_uarts; i++) {
                struct uart_8250_port *up = &serial8250_ports[i];

                up->port.line = i;
                spin_lock_init(&up->port.lock);

                init_timer(&up->timer);
                up->timer.function = serial8250_timeout;

                /*
                 * ALPHA_KLUDGE_MCR needs to be killed.
                 */
                up->mcr_mask = ~ALPHA_KLUDGE_MCR;
                up->mcr_force = ALPHA_KLUDGE_MCR;

                up->port.ops = &serial8250_pops;


who call flush_to_ldisc?
/**
 *      initialize_tty_struct
 *      @tty: tty to initialize
 *
 *      This subroutine initializes a tty structure that has been newly
 *      allocated.
 *
 *      Locking: none - tty in question must not be exposed at this point
 */

static void initialize_tty_struct(struct tty_struct *tty)
{
        memset(tty, 0, sizeof(struct tty_struct));
        tty->magic = TTY_MAGIC;
        tty_ldisc_assign(tty, tty_ldisc_get(N_TTY));
        tty->session = NULL;
        tty->pgrp = NULL;
        tty->overrun_time = jiffies;
        tty->buf.head = tty->buf.tail = NULL;
        tty_buffer_init(tty);
        INIT_DELAYED_WORK(&tty->buf.work, flush_to_ldisc);
        init_MUTEX(&tty->buf.pty_sem);
        mutex_init(&tty->termios_mutex);
        init_waitqueue_head(&tty->write_wait);
        init_waitqueue_head(&tty->read_wait);
        INIT_WORK(&tty->hangup_work, do_tty_hangup);
        mutex_init(&tty->atomic_read_lock);
        mutex_init(&tty->atomic_write_lock);
        spin_lock_init(&tty->read_lock);
        INIT_LIST_HEAD(&tty->tty_files);
        INIT_WORK(&tty->SAK_work, do_SAK_work);
}

:cs find c initialize_tty_struct
/**
 *      init_dev                -       initialise a tty device
 *      @driver: tty driver we are opening a device on
 *      @idx: device index
 *      @tty: returned tty structure
 *
 *      Prepare a tty device. This may not be a "new" clean device but
 *      could also be an active device. The pty drivers require special
 *      handling because of this.
 *
 *      Locking:
 *              The function is called under the tty_mutex, which
 *      protects us from the tty struct or driver itself going away.
 *
 *      On exit the tty device has the line discipline attached and
 *      a reference count of 1. If a pair was created for pty/tty use
 *      and the other was a pty master then it too has a reference count of 1.
 *
 * WSH 06/09/97: Rewritten to remove races and properly clean up after a
 * failed open.  The new code protects the open with a mutex, so it's
 * really quite straightforward.  The mutex locking can probably be
 * relaxed for the (most common) case of reopening a tty.
 */

static int init_dev(struct tty_driver *driver, int idx,
        struct tty_struct **ret_tty)
{
        struct tty_struct *tty, *o_tty;
        struct ktermios *tp, **tp_loc, *o_tp, **o_tp_loc;
        struct ktermios *ltp, **ltp_loc, *o_ltp, **o_ltp_loc;
        int retval = 0; 

        /* check whether we're reopening an existing tty */
        if (driver->flags & TTY_DRIVER_DEVPTS_MEM) { 
                tty = devpts_get_tty(idx);
                /*
                 * If we don't have a tty here on a slave open, it's because
                 * the master already started the close process and there's
                 * no relation between devpts file and tty anymore. 
                 */
                if (!tty && driver->subtype == PTY_TYPE_SLAVE) {
                        retval = -EIO;
                        goto end_init;

		}
                /*
                 * It's safe from now on because init_dev() is called with
                 * tty_mutex held and release_dev() won't change tty->count
                 * or tty->flags without having to grab tty_mutex
                 */
                if (tty && driver->subtype == PTY_TYPE_MASTER)
                        tty = tty->link;
        } else {
                tty = driver->ttys[idx];
        }
        if (tty) goto fast_track;

        /*
         * First time open is complex, especially for PTY devices.
         * This code guarantees that either everything succeeds and the
         * TTY is ready for operation, or else the table slots are vacated
         * and the allocated memory released.  (Except that the termios
         * and locked termios may be retained.)
         */

        if (!try_module_get(driver->owner)) {
                retval = -ENODEV;
                goto end_init;
        }

        o_tty = NULL;
        tp = o_tp = NULL;
        ltp = o_ltp = NULL;

        tty = alloc_tty_struct();
        if(!tty)
                goto fail_no_mem;
        initialize_tty_struct(tty);
        tty->driver = driver;
        tty->index = idx;
        tty_line_name(driver, idx, tty->name);

        if (driver->flags & TTY_DRIVER_DEVPTS_MEM) {
                tp_loc = &tty->termios;
                ltp_loc = &tty->termios_locked;
        } else {
                tp_loc = &driver->termios[idx];
                ltp_loc = &driver->termios_locked[idx];
        }


 if (!*tp_loc) {
                tp = kmalloc(sizeof(struct ktermios), GFP_KERNEL);
                if (!tp)
                        goto free_mem_out;
                *tp = driver->init_termios;
        }

        if (!*ltp_loc) {
                ltp = kzalloc(sizeof(struct ktermios), GFP_KERNEL);
                if (!ltp)
                        goto free_mem_out;
        }

        if (driver->type == TTY_DRIVER_TYPE_PTY) {
                o_tty = alloc_tty_struct(); 
                if (!o_tty) 
                        goto free_mem_out;
                initialize_tty_struct(o_tty);
                o_tty->driver = driver->other;
                o_tty->index = idx;
                tty_line_name(driver->other, idx, o_tty->name);

                if (driver->flags & TTY_DRIVER_DEVPTS_MEM) {
                        o_tp_loc = &o_tty->termios;
                        o_ltp_loc = &o_tty->termios_locked;
                } else {
                        o_tp_loc = &driver->other->termios[idx];
                        o_ltp_loc = &driver->other->termios_locked[idx];
                }

                if (!*o_tp_loc) {
                        o_tp = kmalloc(sizeof(struct ktermios), GFP_KERNEL);
                        if (!o_tp)
                                goto free_mem_out;
                        *o_tp = driver->other->init_termios;
                }

                if (!*o_ltp_loc) {
                        o_ltp = kzalloc(sizeof(struct ktermios), GFP_KERNEL);
                        if (!o_ltp)
                                goto free_mem_out;
                }

                  /*
                 * Everything allocated ... set up the o_tty structure.
                 */
                if (!(driver->other->flags & TTY_DRIVER_DEVPTS_MEM)) {
                        driver->other->ttys[idx] = o_tty;
                }
                if (!*o_tp_loc)
                        *o_tp_loc = o_tp;
                if (!*o_ltp_loc)
                        *o_ltp_loc = o_ltp;
                o_tty->termios = *o_tp_loc;
                o_tty->termios_locked = *o_ltp_loc;
                driver->other->refcount++;
                if (driver->subtype == PTY_TYPE_MASTER)
                        o_tty->count++;

                /* Establish the links in both directions */
                tty->link   = o_tty;
                o_tty->link = tty;
        }

        /*
         * All structures have been allocated, so now we install them.
         * Failures after this point use release_tty to clean up, so
         * there's no need to null out the local pointers.
         */
        if (!(driver->flags & TTY_DRIVER_DEVPTS_MEM)) {
                driver->ttys[idx] = tty;
        }

        if (!*tp_loc)
                *tp_loc = tp;
        if (!*ltp_loc)
                *ltp_loc = ltp;
        tty->termios = *tp_loc;
        tty->termios_locked = *ltp_loc;
        /* Compatibility until drivers always set this */
        tty->termios->c_ispeed = tty_termios_input_baud_rate(tty->termios);
        tty->termios->c_ospeed = tty_termios_baud_rate(tty->termios);
        driver->refcount++;
        tty->count++;

        /*
         * Structures all installed ... call the ldisc open routines.
         * If we fail here just call release_tty to clean up.  No need
         * to decrement the use counts, as release_tty doesn't care.


#define INIT_DELAYED_WORK(_work, _func)                         \
        do {                                                    \
                INIT_WORK(&(_work)->work, (_func));             \
                init_timer(&(_work)->timer);                    \
        } while (0)

#define INIT_DELAYED_WORK_DEFERRABLE(_work, _func)                      \
        do {                                                    \
                INIT_WORK(&(_work)->work, (_func));             \
                init_timer_deferrable(&(_work)->timer);         \
        } while (0)
#define PREPARE_WORK(_work, _func)                              \
        do {                                                    \
                (_work)->func = (_func);                        \
        } while (0)

#define INIT_WORK(_work, _func)                                         \
        do {                                                            \
                (_work)->data = (atomic_long_t) WORK_DATA_INIT();       \
                INIT_LIST_HEAD(&(_work)->entry);                        \
                PREPARE_WORK((_work), (_func));                         \
        } while (0)
#endif  

struct tty_struct {
        int     magic;
        struct tty_driver *driver;
        int index;
        struct tty_ldisc ldisc;
        struct mutex termios_mutex;
        struct ktermios *termios, *termios_locked;
        char name[64];
        struct pid *pgrp;
        struct pid *session;
        unsigned long flags;
        int count;
        struct winsize winsize;
        unsigned char stopped:1, hw_stopped:1, flow_stopped:1, packet:1;
        unsigned char low_latency:1, warned:1;
        unsigned char ctrl_status;
        unsigned int receive_room;      /* Bytes free for queue */

        struct tty_struct *link;
        struct fasync_struct *fasync;
        struct tty_bufhead buf;
        int alt_speed;          /* For magic substitution of 38400 bps */
        wait_queue_head_t write_wait;
        wait_queue_head_t read_wait;
        struct work_struct hangup_work;
        void *disc_data;
        void *driver_data;
        struct list_head tty_files;

#define N_TTY_BUF_SIZE 4096

        /*
         * The following is data for the N_TTY line discipline.  For
         * historical reasons, this is included in the tty structure.
         */
        unsigned int column;
        unsigned char lnext:1, erasing:1, raw:1, real_raw:1, icanon:1;
        unsigned char closing:1;
        unsigned short minimum_to_wake;
        unsigned long overrun_time;
        int num_overrun;
        unsigned long process_char_map[256/(8*sizeof(unsigned long))];
        char *read_buf;
        int read_head;
        int read_tail;
        int read_cnt;
        unsigned long read_flags[N_TTY_BUF_SIZE/(8*sizeof(unsigned long))];
 	int canon_data;
        unsigned long canon_head;
        unsigned int canon_column;
        struct mutex atomic_read_lock;
        struct mutex atomic_write_lock;
        unsigned char *write_buf;
        int write_cnt;
        spinlock_t read_lock;
        /* If the tty has a pending do_SAK, queue it here - akpm */
        struct work_struct SAK_work;
};

struct tty_buffer {
        struct tty_buffer *next;
        char *char_buf_ptr;
        unsigned char *flag_buf_ptr;
        int used;
        int size;
        int commit;
        int read;
        /* Data points here */
        unsigned long data[0];
};

struct tty_bufhead {
        struct delayed_work work;
        struct semaphore pty_sem;
        spinlock_t lock;
        struct tty_buffer *head;        /* Queue head */
        struct tty_buffer *tail;        /* Active buffer */
        struct tty_buffer *free;        /* Free queue head */
        int memory_used;                /* Buffer space used excluding free queue */
};
struct delayed_work {
        struct work_struct work;
        struct timer_list timer;
};
struct work_struct {
        atomic_long_t data;
#define WORK_STRUCT_PENDING 0           /* T if work item pending execution */
#define WORK_STRUCT_FLAG_MASK (3UL)
#define WORK_STRUCT_WQ_DATA_MASK (~WORK_STRUCT_FLAG_MASK)
        struct list_head entry;
        work_func_t func;
#ifdef CONFIG_LOCKDEP
        struct lockdep_map lockdep_map;
#endif
}; 

 strace bash ./echo_a.sh v.s.
strace ./echo_a.sh


execve("./echo_a.sh", ["./echo_a.sh"], [/* 31 vars */]) = 0
brk(0)                                  = 0x80f8000

open("/dev/tty", O_RDWR|O_NONBLOCK|O_LARGEFILE) = 3
close(3)                                = 0
brk(0)                                  = 0x80f8000
brk(0x80f9000)                          = 0x80f9000
                = 0
brk(0x80fd000)                          = 0x80fd000
                    = 0

getuid32()                              = 1000
getgid32()                              = 1000
geteuid32()                             = 1000
getegid32()                             = 1000
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0
time(NULL)                              = 1209285174
open("/proc/meminfo", O_RDONLY)         = 3
fstat64(3, {st_mode=S_IFREG|0444, st_size=0, ...}) = 0
mmap2(NULL, 4096, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0) = 0xb7d04000
read(3, "MemTotal:      1026172 kB\nMemFre"..., 1024) = 728
close(3)                                = 0
munmap(0xb7d04000, 4096)                = 0
brk(0x8103000)                          = 0x8103000
rt_sigaction(SIGCHLD, {SIG_DFL}, {SIG_DFL}, 8) = 0
rt_sigaction(SIGCHLD, {SIG_DFL}, {SIG_DFL}, 8) = 0
rt_sigaction(SIGINT, {SIG_DFL}, {SIG_DFL}, 8) = 0
rt_sigaction(SIGINT, {SIG_DFL}, {SIG_DFL}, 8) = 0
rt_sigaction(SIGQUIT, {SIG_DFL}, {SIG_DFL}, 8) = 0
rt_sigaction(SIGQUIT, {SIG_DFL}, {SIG_DFL}, 8) = 0
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0
rt_sigaction(SIGQUIT, {SIG_IGN}, {SIG_DFL}, 8) = 0
uname({sys="Linux", node="eddie-desktop", ...}) = 0
brk(0x8104000)                          = 0x8104000
brk(0x8105000)                          = 0x8105000
brk(0x8106000)                          = 0x8106000
stat64("/home/eddie/linux", {st_mode=S_IFDIR|0755, st_size=4096, ...}) = 0
stat64(".", {st_mode=S_IFDIR|0755, st_size=4096, ...}) = 0
getpid()                                = 8363
brk(0x8107000)                          = 0x8107000
getppid()                               = 8362
getpgrp()                               = 8362
rt_sigaction(SIGCHLD, {0x807e8b0, [], 0}, {SIG_DFL}, 8) = 0
brk(0x8108000)                          = 0x8108000
brk(0x8109000)                          = 0x8109000
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0


open("./echo_a.sh", O_RDONLY|O_LARGEFILE) = 3
ioctl(3, SNDCTL_TMR_TIMEBASE or TCGETS, 0xbf9a3ee8) = -1 ENOTTY (Inappropriate ioctl for device)
_llseek(3, 0, [0], SEEK_CUR)            = 0
read(3, "#!/bin/bash\necho a > /dev/null;\n"..., 80) = 40
_llseek(3, 0, [0], SEEK_SET)            = 0
getrlimit(RLIMIT_NOFILE, {rlim_cur=1024, rlim_max=1024}) = 0

dup2(3, 255)                            = 255
close(3)                                = 0
fcntl64(255, F_SETFD, FD_CLOEXEC)       = 0
fcntl64(255, F_GETFL)                   = 0x8000 (flags O_RDONLY|O_LARGEFILE)
fstat64(255, {st_mode=S_IFREG|0744, st_size=40, ...}) = 0
_llseek(255, 0, [0], SEEK_CUR)          = 0
brk(0x810a000)                          = 0x810a000
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0
read(255, "#!/bin/bash\necho a > /dev/null;\n"..., 40) = 40
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0

open("/dev/null", O_WRONLY|O_CREAT|O_TRUNC|O_LARGEFILE, 0666) = 3
fcntl64(1, F_GETFD)                     = 0
fcntl64(1, F_DUPFD, 10)                 = 10
fcntl64(1, F_GETFD)                     = 0
fcntl64(10, F_SETFD, FD_CLOEXEC)        = 0

dup2(3, 1)                              = 1
close(3)                                = 0
write(1, "a\n", 2)                      = 2
dup2(10, 1)                             = 1
fcntl64(10, F_GETFD)                    = 0x1 (flags FD_CLOEXEC)
close(10)                               = 0
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0
write(1, "a\n", 2a
)                      = 2
rt_sigprocmask(SIG_BLOCK, NULL, [], 8)  = 0
read(255, "", 40)                       = 0
exit_group(0)                           = ?
Process 8363 detached

