{#if config?.enabled && config.displayMode === 'PAGE'}
  <div class="countdown-timer-countdown-timer-page container py-4 py-md-5">
    <div class="row justify-content-center">
      <div class="col-lg-8">
        <div class="rounded-4 overflow-hidden position-relative" style="{containerStyle}">

          {#if config.backgroundImage}
            <div
              class="position-absolute top-0 start-0 w-100 h-100"
              style="background-image: url(/api/plugins/pano-plugin-countdown-timer/background/{config.backgroundImage}); background-size: cover; background-position: center; z-index: 0; {config.theme === 'BLUR' ? 'filter: blur(8px); transform: scale(1.1);' : ''}">
            </div>
            <div
              class="position-absolute top-0 start-0 w-100 h-100"
              style="{overlayStyle} z-index: 1;">
            </div>
          {/if}

          <div class="text-center p-3 p-sm-4 p-md-5 position-relative" style="z-index: 2; color: {config.textColor || '#ffffff'};">
            {#if config.showLogo && siteInfo}
              <div class="mb-4">
                <img
                  src="/api/v1/website-logo?hash={siteInfo.websiteLogoHash}"
                  alt={siteInfo.websiteName}
                  style="max-height: {logoHeight}px; max-width: min({logoHeight * 2.5}px, 100%); object-fit: contain;" />
              </div>
            {/if}

            <h1 class="countdown-timer-countdown-timer-page__title display-5 fw-bold mb-3" style="overflow-wrap: anywhere;">{config.title || $_('timer.default-title')}</h1>

            <p class="lead mb-4 mb-md-5" style="opacity: 0.8; overflow-wrap: anywhere;">{config.message || $_('timer.default-message')}</p>

            {#if !ended}
              <CountdownDisplay
                {days} {hours} {minutes} {seconds}
                style={config.countdownStyle || 'SIMPLE'}
                size="large"
                isDark={isDarkTheme}
                daysLabel={$_('timer.days')}
                hoursLabel={$_('timer.hours')}
                minsLabel={$_('timer.minutes')}
                secsLabel={$_('timer.seconds')} />
            {:else}
              <div class="countdown-timer-countdown-timer-page__alert alert alert-success d-inline-block">
                <i class="fa fa-check-circle me-2"></i>
                {$_('timer.ended')}
              </div>
            {/if}

            {#if config.showServerIp && siteInfo?.ipAddress}
              <div class="mt-3" style="opacity: 0.7;">
                <small class="text-uppercase" style="letter-spacing: 1px;">{$_('timer.server-ip')}</small>
                <div class="fw-bold fs-5" style="overflow-wrap: anywhere;">{siteInfo.ipAddress}</div>
              </div>
            {/if}
          </div>
        </div>
      </div>
    </div>
  </div>
{/if}

<script>
    import {page} from '@panomc/sdk/svelte';
    import { derived } from 'svelte/store';
    import { _ as i18n } from '@panomc/sdk/utils/language';
    import CountdownDisplay from './CountdownDisplay.svelte';

    // plugin translations: $_('key') reads plugins.pano-plugin-countdown-timer.key
    const _ = derived(i18n, ($_fn) => (key, options) => $_fn(`plugins.pano-plugin-countdown-timer.${key}`, options));

    const browser = typeof window !== 'undefined';

  let config = $derived($page.data.countdownTimerConfig);
  let siteInfo = $derived($page.data.session?.siteInfo);

  function calcTimer(cfg) {
    if (!cfg?.enabled || !cfg.endDate) return { days: 0, hours: 0, minutes: 0, seconds: 0, ended: false };
    const diff = cfg.endDate - Date.now();
    if (diff <= 0) return { days: 0, hours: 0, minutes: 0, seconds: 0, ended: true };
    return {
      days: Math.floor(diff / (1000 * 60 * 60 * 24)),
      hours: Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
      minutes: Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60)),
      seconds: Math.floor((diff % (1000 * 60)) / 1000),
      ended: false,
    };
  }

  const initial = calcTimer($page.data.countdownTimerConfig);
  let days = $state(initial.days);
  let hours = $state(initial.hours);
  let minutes = $state(initial.minutes);
  let seconds = $state(initial.seconds);
  let ended = $state(initial.ended);
  let notified = $state(false);

  const logoSizes = { SMALL: 64, MEDIUM: 128, LARGE: 192, EXTRA_LARGE: 256, HUGE: 384, MASSIVE: 512 };
  let logoHeight = $derived(logoSizes[config?.logoSize] || 128);

  let isDarkTheme = $derived.by(() => {
    const theme = config?.theme || 'DARK';
    return theme === 'DARK' || theme === 'BLUR' || theme === 'GRADIENT';
  });

  let containerStyle = $derived.by(() => {
    if (!config) return '';
    const theme = config.theme || 'DARK';
    const bg = config.backgroundColor || '#000000';
    const hasBgImage = !!config.backgroundImage;

    if (hasBgImage) return 'background: transparent;';

    switch (theme) {
      case 'DARK':
        return `background: ${bg};`;
      case 'LIGHT':
        return `background: ${bg || '#f8f9fa'};`;
      case 'BLUR':
        return `background: rgba(0,0,0,0.6); backdrop-filter: blur(12px);`;
      case 'MINIMAL':
        return `background: ${bg || '#ffffff'};`;
      case 'GRADIENT':
        return `background: linear-gradient(135deg, ${config.gradientStart || '#667eea'}, ${config.gradientEnd || '#764ba2'});`;
      default:
        return `background: rgba(0,0,0,0.85);`;
    }
  });

  let overlayStyle = $derived.by(() => {
    if (!config) return '';
    const theme = config.theme || 'DARK';

    switch (theme) {
      case 'DARK':
        return 'background: rgba(0,0,0,0.75);';
      case 'LIGHT':
        return 'background: rgba(255,255,255,0.85);';
      case 'BLUR':
        return 'background: rgba(0,0,0,0.4); backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px);';
      case 'MINIMAL':
        return 'background: transparent;';
      case 'GRADIENT':
        return `background: linear-gradient(135deg, ${config.gradientStart || '#667eea'}cc, ${config.gradientEnd || '#764ba2'}cc);`;
      default:
        return 'background: rgba(0,0,0,0.75);';
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
    }
  }



  $effect(() => {
    updateTimer();
    const id = setInterval(updateTimer, 1000);

    return () => clearInterval(id);
  });
</script>
