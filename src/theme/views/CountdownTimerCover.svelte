{#if showCover}
  <div
    data-countdown-cover
    class="countdown-timer-countdown-timer-cover position-fixed text-center"
    style="{coverStyle} inset: 0; z-index: 99999; color: {config.textColor || '#ffffff'};">

    {#if config.backgroundImage}
      <div
        class="position-absolute top-0 start-0 w-100 h-100"
        style="background-image: url(/api/plugins/pano-plugin-countdown-timer/background/{config.backgroundImage}); background-size: cover; background-position: center; z-index: -1; {config.theme === 'BLUR' ? 'filter: blur(8px); transform: scale(1.1);' : ''}">
      </div>
    {/if}

    <!-- Three rows: logo on top, counter in the middle, admin link at the bottom. The outer rows share the free
         space, so the counter stays centered while it fits; on a short or narrow screen nothing overlaps and the
         cover scrolls instead. -->
    <div
      class="countdown-timer-countdown-timer-cover__layout w-100 h-100"
      style="position: relative; z-index: 1; display: grid; grid-template-rows: 1fr auto 1fr; overflow-y: auto; overscroll-behavior: contain; padding: 1rem;">

    <!-- Logo: top row -->
    <div class="countdown-timer-countdown-timer-cover__logo d-flex justify-content-center align-items-start" style="padding-top: {config.coverDismissable ? 'max(5vh, 3.5rem)' : 'max(5vh, 1rem)'}; padding-bottom: 1rem;">
      {#if config.showLogo && siteInfo}
        <img
          src="/api/v1/website-logo?hash={siteInfo.websiteLogoHash}"
          alt={siteInfo.websiteName}
          style="max-height: min({logoHeight}px, 30vh); max-width: min({logoHeight * 2.5}px, 100%); object-fit: contain;" />
      {/if}
    </div>

    <!-- Counter: middle row -->
    <div class="countdown-timer-countdown-timer-cover__body d-flex flex-column align-items-center justify-content-center w-100">
      <div class="w-100" style="max-width: 700px;">
        {#if config.title}
          <h1 class="countdown-timer-countdown-timer-cover__title display-4 fw-bold mb-3" style="overflow-wrap: anywhere;">{config.title}</h1>
        {/if}

        {#if config.message}
          <p class="countdown-timer-countdown-timer-cover__message lead mb-4" style="opacity: 0.8; overflow-wrap: anywhere;">{config.message}</p>
        {/if}

        <CountdownDisplay
          {days} {hours} {minutes} {seconds}
          style={config.countdownStyle || 'SIMPLE'}
          size="large"
          isDark={isDarkTheme}
          daysLabel={$_('timer.days')}
          hoursLabel={$_('timer.hours')}
          minsLabel={$_('timer.minutes')}
          secsLabel={$_('timer.seconds')} />

        {#if config.showServerIp && siteInfo?.ipAddress}
          <div class="mt-3" style="opacity: 0.7;">
            <small class="text-uppercase" style="letter-spacing: 1px;">{$_('timer.server-ip')}</small>
            <div class="fw-bold fs-5" style="overflow-wrap: anywhere;">{siteInfo.ipAddress}</div>
          </div>
        {/if}
      </div>
    </div>

    <!-- Admin access info: bottom row -->
    <div class="countdown-timer-countdown-timer-cover__footer d-flex justify-content-center align-items-end" style="padding-top: 1rem; padding-bottom: max(3vh, 0.5rem);">
      {#if config.showAdminLogin !== false}
        {#if isLoggedIn}
          <a href="/panel" target="_blank" rel="noreferrer" style="color: inherit; opacity: 0.4; font-size: 0.8rem; text-decoration: none;"
             onmouseenter={(e) => e.target.style.opacity = '0.8'}
             onmouseleave={(e) => e.target.style.opacity = '0.4'}>
            <i class="fa fa-cog me-1"></i>{$_('timer.admin-panel')}
          </a>
        {:else}
          <a href="/login" style="color: inherit; opacity: 0.4; font-size: 0.8rem; text-decoration: none;"
             onmouseenter={(e) => e.target.style.opacity = '0.8'}
             onmouseleave={(e) => e.target.style.opacity = '0.4'}>
            <i class="fa fa-sign-in me-1"></i>{$_('timer.admin-login')}
          </a>
        {/if}
      {/if}
    </div>
    </div>

    <!-- Dismiss button -->
    {#if config.coverDismissable}
      <button
        type="button"
        class="countdown-timer-countdown-timer-cover__action position-absolute btn"
        style="top: 15px; right: 15px; z-index: 10; color: {config.textColor || '#ffffff'}; font-size: 1.5rem; background: rgba(0,0,0,0.4); border: 1px solid rgba(255,255,255,0.25); border-radius: 50%; width: 44px; height: 44px; display: flex; align-items: center; justify-content: center; padding: 0; cursor: pointer;"
        use:dismissAction
        aria-label="Close">
        <i class="fa fa-times"></i>
      </button>
    {/if}

  </div>
{/if}

<OpeningEffect effectType={config?.openingEffect || 'NONE'} active={showEffect} />

<script module>
  // The full-screen cover before opening; mounts on the theme:top hook, or a theme places it with
  // <PluginBlock id="countdown-timer:CountdownTimerCover" />. It reads the config the plugin put on the page data.
  export const view = { hook: 'theme:top', skipLoad: true, block: true };
</script>

<script>
    import {page} from '@panomc/sdk/svelte';
    import { derived } from 'svelte/store';
    import { _ as i18n } from '@panomc/sdk/utils/language';
    import CountdownDisplay from './CountdownDisplay.svelte';
    import OpeningEffect from './OpeningEffect.svelte';

    // plugin translations: $_('key') reads plugins.pano-plugin-countdown-timer.key
    const _ = derived(i18n, ($_fn) => (key, options) => $_fn(`plugins.pano-plugin-countdown-timer.${key}`, options));

    const browser = typeof window !== 'undefined';

  let config = $derived($page.data.countdownTimerConfig);
  let siteInfo = $derived($page.data.session?.siteInfo);
  let isLoggedIn = $derived(!!$page.data.session?.user);

  function calcTimer(cfg) {
    if (!cfg?.enabled || !cfg.endDate) return { days: 0, hours: 0, minutes: 0, seconds: 0, ended: false };

    const diff = cfg.endDate - Date.now();

    if (diff <= 0) {
      return { days: 0, hours: 0, minutes: 0, seconds: 0, ended: true };
    }

    return {
      days: Math.floor(diff / (1000 * 60 * 60 * 24)),
      hours: Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
      minutes: Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60)),
      seconds: Math.floor((diff % (1000 * 60)) / 1000),
      ended: false,
    };
  }

  // Calculate immediately (works in SSR)
  const initial = calcTimer($page.data.countdownTimerConfig);
  let days = $state(initial.days);
  let hours = $state(initial.hours);
  let minutes = $state(initial.minutes);
  let seconds = $state(initial.seconds);
  let ended = $state(initial.ended);
  let notified = $state(false);
  let showEffect = $state(false);
  let dismissed = $state(false);

  function dismissAction(node) {
    function handleClick() {
      const cover = node.closest('[data-countdown-cover]');
      if (cover) cover.style.display = 'none';
      document.body.style.overflow = '';
      document.body.classList.remove('countdown-minimal-layout');
    }
    node.addEventListener('click', handleClick);
    return {
      destroy() {
        node.removeEventListener('click', handleClick);
      }
    };
  }

  // Logo sizes for cover (fullscreen)
  const logoSizes = { SMALL: 64, MEDIUM: 128, LARGE: 192, EXTRA_LARGE: 256, HUGE: 384, MASSIVE: 512 };
  let logoHeight = $derived(logoSizes[config?.logoSize] || 128);

  let showCover = $derived.by(() => {
    if (!config?.enabled) return false;
    if (config.displayMode !== 'COVER') return false;
    if (ended) return false;

    const currentPath = $page.url?.pathname || '';
    if (currentPath === '/login' || currentPath.startsWith('/login')) return false;

    // Cover scope: HOME_ONLY means only show on home page
    if (config.coverScope === 'HOME_ONLY' && currentPath !== '/' && currentPath !== '') return false;

    return true;
  });

  let isDarkTheme = $derived.by(() => {
    const theme = config?.theme || 'DARK';
    return theme === 'DARK' || theme === 'BLUR' || theme === 'GRADIENT';
  });

  let coverStyle = $derived.by(() => {
    if (!config) return '';
    const theme = config.theme || 'DARK';
    const bg = config.backgroundColor || '#000000';
    const hasBgImage = !!config.backgroundImage;

    switch (theme) {
      case 'DARK':
        return hasBgImage
          ? `background: rgba(0,0,0,0.75);`
          : `background: ${bg}; opacity: 0.95;`;
      case 'LIGHT':
        return hasBgImage
          ? `background: rgba(255,255,255,0.85);`
          : `background: ${bg || '#f8f9fa'};`;
      case 'BLUR':
        return hasBgImage
          ? `background: rgba(0,0,0,0.4); backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px);`
          : `background: rgba(0,0,0,0.6); backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px);`;
      case 'MINIMAL':
        return hasBgImage
          ? `background: transparent;`
          : `background: ${bg || '#ffffff'};`;
      case 'GRADIENT':
        return `background: linear-gradient(135deg, ${config.gradientStart || '#667eea'}, ${config.gradientEnd || '#764ba2'});`;
      default:
        return `background: rgba(0,0,0,0.85);`;
    }
  });

  function updateTimer() {
    const t = calcTimer(config);
    days = t.days;
    hours = t.hours;
    minutes = t.minutes;
    seconds = t.seconds;
    ended = t.ended;

    if (t.ended && !notified && browser) {
      notified = true;

      // Show opening effect before hiding cover
      if (config?.openingEffect && config.openingEffect !== 'NONE') {
        showEffect = true;
        setTimeout(() => {
          showEffect = false;
          ended = true;
        }, 4000);
        // Don't set ended yet — keep cover visible for effect
        ended = false;
      }
    }
  }



  $effect(() => {
    updateTimer();
    const id = setInterval(updateTimer, 1000);

    return () => clearInterval(id);
  });

  // Lock body scroll when cover is visible
  $effect(() => {
    if (!browser) return;

    if (showCover) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }

    return () => {
      document.body.style.overflow = '';
    };
  });

  // On login page during active countdown, add class to hide navbar/footer
  let isCountdownActive = $derived.by(() => {
    if (!config?.enabled || config.displayMode !== 'COVER') return false;
    if (ended) return false;
    return true;
  });

  let isLoginPage = $derived.by(() => {
    const p = $page.url?.pathname || '';
    return p === '/login' || p.startsWith('/login');
  });

  $effect(() => {
    if (!browser) return;

    if (isCountdownActive && isLoginPage) {
      document.body.classList.add('countdown-minimal-layout');
    } else {
      document.body.classList.remove('countdown-minimal-layout');
    }

    return () => {
      document.body.classList.remove('countdown-minimal-layout');
    };
  });
</script>